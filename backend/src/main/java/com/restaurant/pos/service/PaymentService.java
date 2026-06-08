package com.restaurant.pos.service;

import com.restaurant.pos.entity.Payment;
import com.restaurant.pos.entity.PaymentMethod;
import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    
    /** Processes a payment for an order using its persisted amount. Handles point balance modifications, point accruals, tier upgrades, and status transition. */
    Payment processPayment(UUID orderId, PaymentMethod method, BigDecimal amount);
    
    /** Refunds a payment and updates the corresponding order/customer statistics. */
    Payment refundPayment(UUID paymentId);
}
