package com.restaurant.pos.dto.order;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID id,
        UUID menuItemId,
        String menuItemName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal,
        String notes,
        boolean promotionalItem
) {}
