package com.restaurant.pos.dto.payment;

import com.restaurant.pos.entity.PaymentMethod;
import com.restaurant.pos.entity.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        UUID orderId,
        PaymentMethod paymentMethod,
        BigDecimal amount,
        PaymentStatus status,
        LocalDateTime paidAt
) {}
