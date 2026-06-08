package com.restaurant.pos.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyRevenueResponse(
        LocalDate date,
        long orderCount,
        BigDecimal totalRevenue
) {}
