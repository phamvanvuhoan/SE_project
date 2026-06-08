package com.restaurant.pos.dto.order;

import com.restaurant.pos.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public class OrderStatusTransitionRequest {

    @NotNull(message = "Target status is required")
    private OrderStatus targetStatus;

    public OrderStatusTransitionRequest() {}

    public OrderStatus getTargetStatus() { return targetStatus; }
    public void setTargetStatus(OrderStatus targetStatus) { this.targetStatus = targetStatus; }
}
