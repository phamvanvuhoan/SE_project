package com.restaurant.pos.controller;

import com.restaurant.pos.dto.common.ApiResponse;
import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.customer.CreateCustomerRequest;
import com.restaurant.pos.dto.customer.CustomerResponse;
import com.restaurant.pos.dto.customer.UpdateCustomerRequest;
import com.restaurant.pos.service.CustomerCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/v1/customers")
@Tag(name = "Customers")
public class CustomerController {

    private final CustomerCrudService customerService;

    public CustomerController(CustomerCrudService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @Operation(summary = "List or search customers")
    public ApiResponse<PagedResponse<CustomerResponse>> findAll(@RequestParam(required = false) String keyword, Pageable pageable) {
        return ApiResponse.success(customerService.findAll(keyword, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer details")
    public ApiResponse<CustomerResponse> findById(@PathVariable UUID id) {
        return ApiResponse.success(customerService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create customer")
    public ResponseEntity<ApiResponse<CustomerResponse>> create(@Valid @RequestBody CreateCustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer created successfully", customerService.create(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer")
    public ApiResponse<CustomerResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateCustomerRequest request) {
        return ApiResponse.success("Customer updated successfully", customerService.update(id, request));
    }
}
