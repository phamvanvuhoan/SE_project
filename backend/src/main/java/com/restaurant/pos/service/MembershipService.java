package com.restaurant.pos.service;

import com.restaurant.pos.entity.Customer;
import java.math.BigDecimal;

public interface MembershipService {

    /** Calculates points redemption limit based on customer balance and order total. */
    BigDecimal calculateMaxPointsRedemption(Customer customer, BigDecimal orderTotal);

    /**
     * Credits loyalty points to customer after payment and triggers membership tier re-evaluation.
     *
     * <p><b>Design decision (explicit):</b>
     * <ul>
     *   <li>{@code grossValueForTier} — the pre-redemption economic value of the order
     *       ({@code subtotal - promotionDiscount}). Used for {@code total_spent} tracking so
     *       that membership tier reflects what the customer consumed, not the net cash paid.</li>
     *   <li>{@code cashPaid} — the actual cash amount exchanged ({@code totalAmount},
     *       after point redemption). Used for the earned-points formula so that customers
     *       do not earn points on the value of points they already redeemed.</li>
     * </ul>
     */
    void accruePointsAndUpgrade(Customer customer, BigDecimal grossValueForTier, BigDecimal cashPaid);

    /**
     * Reverses loyalty changes that were applied during a payment that is now refunded.
     * Deducts the earned points, restores the redeemed points, reduces total_spent, and
     * re-evaluates membership tier.
     *
     * @param customer         the customer whose record must be corrected
     * @param earnedPoints     points to deduct (originally awarded at payment time)
     * @param redeemedPoints   points to restore (originally consumed at payment time)
     * @param grossValueForTier the amount to subtract from total_spent
     */
    void reversePointsForRefund(Customer customer, int earnedPoints, int redeemedPoints, BigDecimal grossValueForTier);
}
