package com.restaurant.pos.service;

import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.order.AddOrderItemRequest;
import com.restaurant.pos.dto.order.ApplyPointsRequest;
import com.restaurant.pos.dto.order.CreateOrderRequest;
import com.restaurant.pos.dto.order.OrderResponse;
import com.restaurant.pos.dto.order.OrderStatusTransitionRequest;
import com.restaurant.pos.dto.order.UpdateOrderItemRequest;
import com.restaurant.pos.dto.payment.PaymentResponse;
import com.restaurant.pos.dto.payment.ProcessPaymentRequest;
import com.restaurant.pos.entity.OrderStatus;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderCrudService {
    PagedResponse<OrderResponse> findAll(OrderStatus status, Pageable pageable);
    OrderResponse findById(UUID id);
    OrderResponse create(CreateOrderRequest request);
    OrderResponse addItem(UUID id, AddOrderItemRequest request);
    OrderResponse updateItem(UUID id, UUID itemId, UpdateOrderItemRequest request);
    OrderResponse removeItem(UUID id, UUID itemId);
    OrderResponse applyPoints(UUID id, ApplyPointsRequest request);
    OrderResponse transition(UUID id, OrderStatusTransitionRequest request);
    PaymentResponse processPayment(UUID id, ProcessPaymentRequest request);
}
