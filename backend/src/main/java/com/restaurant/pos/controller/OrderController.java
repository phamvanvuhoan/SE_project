package com.restaurant.pos.controller;

import com.restaurant.pos.dto.common.ApiResponse;
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
import com.restaurant.pos.service.OrderCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders")
public class OrderController {

    private final OrderCrudService orderService;

    public OrderController(OrderCrudService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "List orders")
    public ApiResponse<PagedResponse<OrderResponse>> findAll(@RequestParam(required = false) OrderStatus status, Pageable pageable) {
        return ApiResponse.success(orderService.findAll(status, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order details")
    public ApiResponse<OrderResponse> findById(@PathVariable UUID id) {
        return ApiResponse.success(orderService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create order")
    public ResponseEntity<ApiResponse<OrderResponse>> create(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created successfully", orderService.create(request)));
    }

    @PostMapping("/{id}/items")
    @Operation(summary = "Add item to order")
    public ApiResponse<OrderResponse> addItem(@PathVariable UUID id, @Valid @RequestBody AddOrderItemRequest request) {
        return ApiResponse.success("Order item added successfully", orderService.addItem(id, request));
    }

    @PutMapping("/{id}/items/{itemId}")
    @Operation(summary = "Update order item quantity")
    public ApiResponse<OrderResponse> updateItem(@PathVariable UUID id,
                                                 @PathVariable UUID itemId,
                                                 @Valid @RequestBody UpdateOrderItemRequest request) {
        return ApiResponse.success("Order item updated successfully", orderService.updateItem(id, itemId, request));
    }

    @DeleteMapping("/{id}/items/{itemId}")
    @Operation(summary = "Remove item from order")
    public ApiResponse<OrderResponse> removeItem(@PathVariable UUID id, @PathVariable UUID itemId) {
        return ApiResponse.success("Order item removed successfully", orderService.removeItem(id, itemId));
    }

    @PostMapping("/{id}/redeem-points")
    @Operation(summary = "Redeem membership points")
    public ApiResponse<OrderResponse> applyPoints(@PathVariable UUID id, @Valid @RequestBody ApplyPointsRequest request) {
        return ApiResponse.success("Points redemption applied successfully", orderService.applyPoints(id, request));
    }

    @PostMapping("/{id}/transition")
    @Operation(summary = "Transition order status")
    public ApiResponse<OrderResponse> transition(@PathVariable UUID id, @Valid @RequestBody OrderStatusTransitionRequest request) {
        return ApiResponse.success("Order status transitioned successfully", orderService.transition(id, request));
    }

    @PostMapping("/{id}/payments")
    @Operation(summary = "Process order payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(@PathVariable UUID id,
                                                                       @Valid @RequestBody ProcessPaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Payment processed successfully", orderService.processPayment(id, request)));
    }
}
