package com.restaurant.pos.repository;

import com.restaurant.pos.entity.Order;
import com.restaurant.pos.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    List<Order> findByCustomerId(UUID customerId);

    List<Order> findByEmployeeId(UUID employeeId);

    List<Order> findByTableId(UUID tableId);

    @Query("SELECT o FROM Order o WHERE o.status = :status " +
           "AND o.orderTime BETWEEN :from AND :to")
    Page<Order> findByStatusAndDateRange(
            @Param("status") OrderStatus status,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.orderTime BETWEEN :from AND :to")
    List<Order> findByOrderTimeBetween(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
