package com.restaurant.pos.dto.report;

import java.math.BigDecimal;
import java.util.UUID;

public record TopDishResponse(
        UUID dishId,
        String dishName,
        long quantitySold,
        BigDecimal totalRevenue
) {}
