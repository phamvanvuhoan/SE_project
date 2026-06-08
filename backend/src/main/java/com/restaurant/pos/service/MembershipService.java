package com.restaurant.pos.service;

import com.restaurant.pos.entity.Customer;
import java.math.BigDecimal;

public interface MembershipService {
    
    /** Calculates points redemption limit based on customer balance and order total. */
    BigDecimal calculateMaxPointsRedemption(Customer customer, BigDecimal orderTotal);
    
    /** Credits loyalty points to customer after payment and triggers membership tier re-evaluation. */
    void accruePointsAndUpgrade(Customer customer, BigDecimal orderAmountPaid);
}
