package com.restaurant.pos.service.impl;

import com.restaurant.pos.entity.Customer;
import com.restaurant.pos.entity.MembershipLevel;
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

    private final CustomerRepository customerRepository;
    private final MembershipLevelRepository membershipLevelRepository;

    public MembershipServiceImpl(CustomerRepository customerRepository, MembershipLevelRepository membershipLevelRepository) {
        this.customerRepository = customerRepository;
        this.membershipLevelRepository = membershipLevelRepository;
    }

    @Override
    public BigDecimal calculateMaxPointsRedemption(Customer customer, BigDecimal orderTotal) {
        if (customer == null) {
            return BigDecimal.ZERO;
        }
        
        // 1 Point = 1 VND
        BigDecimal customerPointsVal = BigDecimal.valueOf(customer.getTotalPoints());
        BigDecimal limit = BigDecimal.valueOf(500000); // Max 500,000 VND/points per order

        BigDecimal minVal = customerPointsVal.min(orderTotal).min(limit);
        return minVal.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional
    public void accruePointsAndUpgrade(Customer customer, BigDecimal orderAmountPaid) {
        if (customer == null) {
            return;
        }

        // Initialize membership if missing
        if (customer.getMembershipLevel() == null) {
            MembershipLevel defaultLevel = membershipLevelRepository.findByLevelName("Bronze")
                    .orElseGet(() -> {
                        List<MembershipLevel> levels = membershipLevelRepository.findAll();
                        return levels.isEmpty() ? null : levels.get(0);
                    });
            customer.setMembershipLevel(defaultLevel);
        }

        BigDecimal pointRate = BigDecimal.ZERO;
        if (customer.getMembershipLevel() != null) {
            pointRate = customer.getMembershipLevel().getPointRate();
        }

        // Earned points = orderAmountPaid * (pointRate / 100)
        // Point rates are stored as e.g. 10.00 for 10%
        BigDecimal earnedPointsDec = orderAmountPaid.multiply(pointRate)
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
        int earnedPoints = earnedPointsDec.intValue();

        // Update customer details
        customer.setTotalSpent(customer.getTotalSpent().add(orderAmountPaid));
        customer.setTotalPoints(customer.getTotalPoints() + earnedPoints);

        // Evaluate upgrade
        List<MembershipLevel> eligibleLevels = membershipLevelRepository
                .findByMinSpendLessThanEqualOrderByMinSpendDesc(customer.getTotalSpent());
        
        if (!eligibleLevels.isEmpty()) {
            // The first item is the highest tier since we order by minSpend DESC
            customer.setMembershipLevel(eligibleLevels.get(0));
        }

        customerRepository.save(customer);
    }
}
