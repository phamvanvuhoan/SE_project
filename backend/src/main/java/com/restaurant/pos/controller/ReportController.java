package com.restaurant.pos.controller;

import com.restaurant.pos.dto.common.ApiResponse;
import com.restaurant.pos.dto.report.DailyRevenueResponse;
import com.restaurant.pos.dto.report.MonthlyRevenueResponse;
import com.restaurant.pos.dto.report.TopDishResponse;
import com.restaurant.pos.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "Reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/revenue/daily")
    @Operation(summary = "View daily revenue")
    public ApiResponse<DailyRevenueResponse> dailyRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.success(reportService.dailyRevenue(date));
    }

    @GetMapping("/revenue/monthly")
    @Operation(summary = "View monthly revenue")
    public ApiResponse<MonthlyRevenueResponse> monthlyRevenue(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return ApiResponse.success(reportService.monthlyRevenue(month));
    }

    @GetMapping("/dishes/top")
    @Operation(summary = "View top selling dishes")
    public ApiResponse<List<TopDishResponse>> topDishes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ApiResponse.success(reportService.topDishes(from, to));
    }
}
