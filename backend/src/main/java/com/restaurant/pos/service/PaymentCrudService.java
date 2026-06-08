package com.restaurant.pos.service;

import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.payment.PaymentResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentCrudService {
    PagedResponse<PaymentResponse> findAll(Pageable pageable);
    PaymentResponse findById(UUID id);
    PaymentResponse refund(UUID id);
}
