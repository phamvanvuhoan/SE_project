package com.restaurant.pos.dto.order;

import com.restaurant.pos.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID tableId,
        String tableNumber,
        UUID employeeId,
        String employeeName,
        UUID customerId,
        String customerName,
        OrderStatus status,
        BigDecimal subtotal,
        BigDecimal promotionDiscount,
        BigDecimal pointDiscount,
        BigDecimal totalAmount,
        LocalDateTime orderTime,
        List<OrderItemResponse> items
) {}
