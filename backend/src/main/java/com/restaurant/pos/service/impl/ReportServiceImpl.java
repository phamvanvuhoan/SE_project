package com.restaurant.pos.service.impl;

import com.restaurant.pos.dto.report.DailyRevenueResponse;
import com.restaurant.pos.dto.report.MonthlyRevenueResponse;
import com.restaurant.pos.dto.report.TopDishResponse;
import com.restaurant.pos.entity.PaymentStatus;
import com.restaurant.pos.repository.OrderItemRepository;
import com.restaurant.pos.repository.PaymentRepository;
import com.restaurant.pos.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final PaymentRepository paymentRepository;
    private final OrderItemRepository orderItemRepository;

    public ReportServiceImpl(PaymentRepository paymentRepository, OrderItemRepository orderItemRepository) {
        this.paymentRepository = paymentRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public DailyRevenueResponse dailyRevenue(LocalDate date) {
        LocalDate target = date == null ? LocalDate.now() : date;
        return paymentRepository.summarizeDailyRevenue(
                target,
                PaymentStatus.COMPLETED,
                target.atStartOfDay(),
                target.plusDays(1).atStartOfDay(),
                BigDecimal.ZERO
        );
    }

    @Override
    public MonthlyRevenueResponse monthlyRevenue(YearMonth month) {
        YearMonth target = month == null ? YearMonth.now() : month;
        return paymentRepository.summarizeMonthlyRevenue(
                target.getYear(),
                target.getMonthValue(),
                PaymentStatus.COMPLETED,
                target.atDay(1).atStartOfDay(),
                target.plusMonths(1).atDay(1).atStartOfDay(),
                BigDecimal.ZERO
        );
    }

    @Override
    public List<TopDishResponse> topDishes(LocalDate from, LocalDate to) {
        LocalDate startDate = from == null ? LocalDate.now() : from;
        LocalDate endDate = to == null ? startDate : to;
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();
        return orderItemRepository.summarizeTopSellingDishes(start, end);
    }
}
