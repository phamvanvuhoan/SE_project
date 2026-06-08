package com.restaurant.pos.service.impl;

import com.restaurant.pos.dto.report.DailyRevenueResponse;
import com.restaurant.pos.dto.report.MonthlyRevenueResponse;
import com.restaurant.pos.dto.report.TopDishResponse;
import com.restaurant.pos.entity.OrderItem;
import com.restaurant.pos.entity.Payment;
import com.restaurant.pos.entity.PaymentStatus;
import com.restaurant.pos.repository.OrderRepository;
import com.restaurant.pos.repository.PaymentRepository;
import com.restaurant.pos.service.ReportService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ReportServiceImpl implements ReportService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public ReportServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public DailyRevenueResponse dailyRevenue(LocalDate date) {
        LocalDate target = date == null ? LocalDate.now() : date;
        List<Payment> payments = completedPaymentsBetween(target.atStartOfDay(), target.plusDays(1).atStartOfDay());
        BigDecimal revenue = payments.stream().map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new DailyRevenueResponse(target, payments.size(), revenue);
    }

    @Override
    public MonthlyRevenueResponse monthlyRevenue(YearMonth month) {
        YearMonth target = month == null ? YearMonth.now() : month;
        List<Payment> payments = completedPaymentsBetween(target.atDay(1).atStartOfDay(), target.plusMonths(1).atDay(1).atStartOfDay());
        BigDecimal revenue = payments.stream().map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new MonthlyRevenueResponse(target.getYear(), target.getMonthValue(), payments.size(), revenue);
    }

    @Override
    public List<TopDishResponse> topDishes(LocalDate from, LocalDate to) {
        LocalDate startDate = from == null ? LocalDate.now() : from;
        LocalDate endDate = to == null ? startDate : to;
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();
        Map<UUID, DishStats> stats = new HashMap<>();
        orderRepository.findByOrderTimeBetween(start, end).forEach(order ->
                order.getOrderItems().stream()
                        .filter(item -> !item.isPromotionalItem() && item.getMenuItem() != null)
                        .forEach(item -> accumulate(stats, item)));
        return stats.values().stream()
                .sorted(Comparator.comparingLong(DishStats::quantitySold).reversed())
                .map(s -> new TopDishResponse(s.dishId(), s.dishName(), s.quantitySold(), s.totalRevenue()))
                .toList();
    }

    private List<Payment> completedPaymentsBetween(LocalDateTime from, LocalDateTime to) {
        return paymentRepository.findByStatus(PaymentStatus.COMPLETED).stream()
                .filter(payment -> payment.getPaidAt() != null)
                .filter(payment -> !payment.getPaidAt().isBefore(from) && payment.getPaidAt().isBefore(to))
                .toList();
    }

    private void accumulate(Map<UUID, DishStats> stats, OrderItem item) {
        UUID id = item.getMenuItem().getId();
        DishStats current = stats.getOrDefault(id, new DishStats(id, item.getMenuItem().getDishName(), 0, BigDecimal.ZERO));
        stats.put(id, new DishStats(id, current.dishName(), current.quantitySold() + item.getQuantity(),
                current.totalRevenue().add(item.getSubtotal())));
    }

    private record DishStats(UUID dishId, String dishName, long quantitySold, BigDecimal totalRevenue) {}
}
