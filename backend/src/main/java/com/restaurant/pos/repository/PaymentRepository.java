package com.restaurant.pos.repository;

import com.restaurant.pos.dto.report.DailyRevenueResponse;
import com.restaurant.pos.dto.report.MonthlyRevenueResponse;
import com.restaurant.pos.entity.Payment;
import com.restaurant.pos.entity.PaymentMethod;
import com.restaurant.pos.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByOrderId(UUID orderId);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);

    @Query("""
            SELECT new com.restaurant.pos.dto.report.DailyRevenueResponse(
                :date,
                COUNT(p),
                COALESCE(SUM(p.amount), :zero)
            )
            FROM Payment p
            WHERE p.status = :status
              AND p.paidAt >= :from
              AND p.paidAt < :to
            """)
    DailyRevenueResponse summarizeDailyRevenue(
            @Param("date") LocalDate date,
            @Param("status") PaymentStatus status,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("zero") BigDecimal zero);

    @Query("""
            SELECT new com.restaurant.pos.dto.report.MonthlyRevenueResponse(
                :year,
                :month,
                COUNT(p),
                COALESCE(SUM(p.amount), :zero)
            )
            FROM Payment p
            WHERE p.status = :status
              AND p.paidAt >= :from
              AND p.paidAt < :to
            """)
    MonthlyRevenueResponse summarizeMonthlyRevenue(
            @Param("year") int year,
            @Param("month") int month,
            @Param("status") PaymentStatus status,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("zero") BigDecimal zero);
}
