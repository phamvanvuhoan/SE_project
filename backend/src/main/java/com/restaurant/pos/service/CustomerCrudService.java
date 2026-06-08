package com.restaurant.pos.service;

import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.customer.CreateCustomerRequest;
import com.restaurant.pos.dto.customer.CustomerResponse;
import com.restaurant.pos.dto.customer.UpdateCustomerRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CustomerCrudService {
    PagedResponse<CustomerResponse> findAll(String keyword, Pageable pageable);
    CustomerResponse findById(UUID id);
    CustomerResponse create(CreateCustomerRequest request);
    CustomerResponse update(UUID id, UpdateCustomerRequest request);
}
