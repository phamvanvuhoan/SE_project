package com.restaurant.pos.repository;

import com.restaurant.pos.entity.OrderEvent;
import com.restaurant.pos.entity.OrderEventId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderEventRepository extends JpaRepository<OrderEvent, OrderEventId> {

    List<OrderEvent> findByOrderId(UUID orderId);

    List<OrderEvent> findByEventPromotionId(UUID eventId);

    /** Deletes all OrderEvents for an order before re-persisting the recalculated set. */
    @Modifying
    @Transactional
    @Query("DELETE FROM OrderEvent oe WHERE oe.order.id = :orderId")
    void deleteByOrderId(@Param("orderId") UUID orderId);
}
