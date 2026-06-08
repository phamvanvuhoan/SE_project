package com.restaurant.pos.controller;

import com.restaurant.pos.dto.common.ApiResponse;
import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.payment.PaymentResponse;
import com.restaurant.pos.service.PaymentCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payments")
public class PaymentController {

    private final PaymentCrudService paymentService;

    public PaymentController(PaymentCrudService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    @Operation(summary = "List payments")
    public ApiResponse<PagedResponse<PaymentResponse>> findAll(Pageable pageable) {
        return ApiResponse.success(paymentService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment details")
    public ApiResponse<PaymentResponse> findById(@PathVariable UUID id) {
        return ApiResponse.success(paymentService.findById(id));
    }

    @PostMapping("/{id}/refund")
    @Operation(summary = "Refund payment")
    public ApiResponse<PaymentResponse> refund(@PathVariable UUID id) {
        return ApiResponse.success("Payment refunded successfully", paymentService.refund(id));
    }
}
