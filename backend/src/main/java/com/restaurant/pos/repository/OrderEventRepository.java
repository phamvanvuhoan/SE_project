package com.restaurant.pos.repository;

import com.restaurant.pos.entity.OrderEvent;
import com.restaurant.pos.entity.OrderEventId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderEventRepository extends JpaRepository<OrderEvent, OrderEventId> {

    List<OrderEvent> findByOrderId(UUID orderId);

    List<OrderEvent> findByEventPromotionId(UUID eventId);
}
