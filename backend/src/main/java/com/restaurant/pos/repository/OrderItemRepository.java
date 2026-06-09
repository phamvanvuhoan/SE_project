package com.restaurant.pos.repository;

import com.restaurant.pos.dto.report.TopDishResponse;
import com.restaurant.pos.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

    List<OrderItem> findByOrderId(UUID orderId);

    List<OrderItem> findByMenuItemId(UUID menuItemId);

    @Query("""
            SELECT new com.restaurant.pos.dto.report.TopDishResponse(
                menuItem.id,
                menuItem.dishName,
                SUM(item.quantity),
                SUM(item.subtotal)
            )
            FROM OrderItem item
            JOIN item.menuItem menuItem
            JOIN item.order orderHeader
            WHERE item.promotionalItem = false
              AND orderHeader.orderTime >= :from
              AND orderHeader.orderTime < :to
            GROUP BY menuItem.id, menuItem.dishName
            ORDER BY SUM(item.quantity) DESC, SUM(item.subtotal) DESC
            """)
    List<TopDishResponse> summarizeTopSellingDishes(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
