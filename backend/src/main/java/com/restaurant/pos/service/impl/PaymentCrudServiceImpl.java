package com.restaurant.pos.service.impl;

import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.payment.PaymentResponse;
import com.restaurant.pos.entity.Payment;
import com.restaurant.pos.exception.ResourceNotFoundException;
import com.restaurant.pos.repository.PaymentRepository;
import com.restaurant.pos.service.PageResponseFactory;
import com.restaurant.pos.service.PaymentCrudService;
import com.restaurant.pos.service.PaymentService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentCrudServiceImpl implements PaymentCrudService {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    public PaymentCrudServiceImpl(PaymentRepository paymentRepository, PaymentService paymentService) {
        this.paymentRepository = paymentRepository;
        this.paymentService = paymentService;
    }

    @Override
    public PagedResponse<PaymentResponse> findAll(Pageable pageable) {
        return PageResponseFactory.fromPage(paymentRepository.findAll(pageable), PaymentCrudServiceImpl::toResponseStatic);
    }

    @Override
    public PaymentResponse findById(UUID id) {
        return toResponseStatic(paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id)));
    }

    @Override
    @Transactional
    public PaymentResponse refund(UUID id) {
        return toResponseStatic(paymentService.refundPayment(id));
    }

    static PaymentResponse toResponseStatic(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder() == null ? null : payment.getOrder().getId(),
                payment.getPaymentMethod(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaidAt()
        );
    }
}
