package com.restaurant.pos.service;

import com.restaurant.pos.dto.report.DailyRevenueResponse;
import com.restaurant.pos.dto.report.MonthlyRevenueResponse;
import com.restaurant.pos.dto.report.TopDishResponse;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface ReportService {
    DailyRevenueResponse dailyRevenue(LocalDate date);
    MonthlyRevenueResponse monthlyRevenue(YearMonth month);
    List<TopDishResponse> topDishes(LocalDate from, LocalDate to);
}
