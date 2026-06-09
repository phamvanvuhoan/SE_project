package com.restaurant.pos.dto.report;

import java.math.BigDecimal;

public record MonthlyRevenueResponse(
        Integer year,
        Integer month,
        Long orderCount,
        BigDecimal totalRevenue
) {}
