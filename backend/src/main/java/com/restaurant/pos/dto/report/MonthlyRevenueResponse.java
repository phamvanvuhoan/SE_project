package com.restaurant.pos.dto.report;

import java.math.BigDecimal;

public record MonthlyRevenueResponse(
        int year,
        int month,
        long orderCount,
        BigDecimal totalRevenue
) {}
