package com.restaurant.pos.service.impl;

import com.restaurant.pos.entity.Customer;
import com.restaurant.pos.entity.MembershipLevel;
import com.restaurant.pos.exception.BusinessRuleViolationException;
import com.restaurant.pos.repository.CustomerRepository;
import com.restaurant.pos.repository.MembershipLevelRepository;
import com.restaurant.pos.service.MembershipService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class MembershipServiceImpl implements MembershipService {

    /** Maximum value of a single point-redemption per order (1 point = 1 VND). */
    private static final BigDecimal MAX_POINT_REDEMPTION = BigDecimal.valueOf(500_000);
    /** Maximum legal pointRate value in the DB (enforced by V3 CHECK constraint). */
    private static final BigDecimal MAX_POINT_RATE = BigDecimal.valueOf(100);

    private final CustomerRepository customerRepository;
    private final MembershipLevelRepository membershipLevelRepository;

    public MembershipServiceImpl(CustomerRepository customerRepository,
                                 MembershipLevelRepository membershipLevelRepository) {
        this.customerRepository = customerRepository;
        this.membershipLevelRepository = membershipLevelRepository;
    }

    @Override
    public BigDecimal calculateMaxPointsRedemption(Customer customer, BigDecimal orderTotal) {
        if (customer == null) {
            return BigDecimal.ZERO;
        }
        // 1 Point = 1 VND; cap at balance, order total, and absolute maximum.
        BigDecimal customerPointsVal = BigDecimal.valueOf(customer.getTotalPoints());
        BigDecimal maxAllowed = customerPointsVal.min(orderTotal).min(MAX_POINT_REDEMPTION);
        return maxAllowed.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void accruePointsAndUpgrade(Customer customer, BigDecimal grossValueForTier, BigDecimal cashPaid) {
        if (customer == null) {
            return;
        }

        // Initialize membership to the lowest qualifying tier for zero spend, not
        // an arbitrary list position.  Fixes the findAll() ordering hazard.
        if (customer.getMembershipLevel() == null) {
            MembershipLevel lowestTier = membershipLevelRepository
                    .findByMinSpendLessThanEqualOrderByMinSpendDesc(BigDecimal.ZERO)
                    .stream().reduce((first, second) -> second) // last = lowest minSpend
                    .orElse(null);
            customer.setMembershipLevel(lowestTier);
        }

        BigDecimal pointRate = BigDecimal.ZERO;
        if (customer.getMembershipLevel() != null) {
            pointRate = customer.getMembershipLevel().getPointRate();
            // Guard against misconfigured rate (e.g. 0.10 stored instead of 10.00).
            // V3 migration adds CHECK (point_rate <= 100) but validate defensively here.
            if (pointRate.compareTo(MAX_POINT_RATE) > 0) {
                throw new BusinessRuleViolationException(
                        "Membership point rate exceeds maximum (100%): " + pointRate
                        + " on level " + customer.getMembershipLevel().getLevelName());
            }
        }

        // Earned points use cashPaid (money the customer actually handed over).
        // This ensures customers do not earn points on the value of points they redeemed.
        BigDecimal earnedPointsDec = cashPaid.multiply(pointRate)
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
        int earnedPoints = earnedPointsDec.intValue();

        // total_spent uses grossValueForTier (economic value consumed, pre-redemption)
        // so that tier upgrades reflect what the customer bought, not what cash changed hands.
        customer.setTotalSpent(customer.getTotalSpent().add(grossValueForTier));
        customer.setTotalPoints(customer.getTotalPoints() + earnedPoints);

        reevaluateTier(customer);
        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void reversePointsForRefund(Customer customer, int earnedPoints, int redeemedPoints,
                                       BigDecimal grossValueForTier) {
        if (customer == null) {
            return;
        }

        // Deduct earned points (were credited at payment time)
        customer.setTotalPoints(Math.max(0, customer.getTotalPoints() - earnedPoints));

        // Restore redeemed points (were consumed at payment time)
        customer.setTotalPoints(customer.getTotalPoints() + redeemedPoints);

        // Reduce total_spent — cannot go below zero
        BigDecimal newSpent = customer.getTotalSpent().subtract(grossValueForTier);
        customer.setTotalSpent(newSpent.max(BigDecimal.ZERO));

        reevaluateTier(customer);
        customerRepository.save(customer);
    }

    /** Re-evaluates and sets the highest membership tier the customer qualifies for. */
    private void reevaluateTier(Customer customer) {
        List<MembershipLevel> eligibleLevels = membershipLevelRepository
                .findByMinSpendLessThanEqualOrderByMinSpendDesc(customer.getTotalSpent());
        if (!eligibleLevels.isEmpty()) {
            // First element = highest qualifying tier (ordered by minSpend DESC)
            customer.setMembershipLevel(eligibleLevels.get(0));
        }
    }
}
