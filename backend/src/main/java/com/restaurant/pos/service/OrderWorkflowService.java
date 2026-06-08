package com.restaurant.pos.service;

import com.restaurant.pos.entity.Order;
import com.restaurant.pos.entity.OrderStatus;
import java.util.UUID;

public interface OrderWorkflowService {
    
    /** Transitions order status with validation against API guideline state transition rules. */
    Order transitionOrderStatus(UUID orderId, OrderStatus targetStatus);
}
