package com.restaurant.pos.service.impl;

import com.restaurant.pos.entity.*;
import com.restaurant.pos.exception.BusinessRuleViolationException;
import com.restaurant.pos.exception.InvalidOrderStateException;
import com.restaurant.pos.exception.ResourceNotFoundException;
import com.restaurant.pos.repository.OrderRepository;
import com.restaurant.pos.repository.PaymentRepository;
import com.restaurant.pos.service.MembershipService;
import com.restaurant.pos.service.OrderWorkflowService;
import com.restaurant.pos.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderWorkflowService orderWorkflowService;
    private final MembershipService membershipService;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              OrderRepository orderRepository,
                              OrderWorkflowService orderWorkflowService,
                              MembershipService membershipService) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.orderWorkflowService = orderWorkflowService;
        this.membershipService = membershipService;
    }

    @Override
    @Transactional
    public Payment processPayment(UUID orderId, PaymentMethod method, BigDecimal amount) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (order.getStatus() != OrderStatus.READY) {
            throw new InvalidOrderStateException("Order must be in READY status to complete payment. Current status: " + order.getStatus());
        }

        // Use persisted order amount
        BigDecimal payableAmount = order.getTotalAmount();
        if (amount.compareTo(payableAmount) < 0) {
            throw new BusinessRuleViolationException("Paid amount is less than the payable order amount: " + payableAmount);
        }

        // Create Payment
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setOrder(order);
        payment.setPaymentMethod(method);
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaidAt(LocalDateTime.now());

        // Update customer membership points
        Customer customer = order.getCustomer();
        if (customer != null) {
            // 1. Deduct redeemed points
            int redeemedPoints = order.getPointDiscount().intValue();
            if (redeemedPoints > 0) {
                if (customer.getTotalPoints() < redeemedPoints) {
                    throw new BusinessRuleViolationException("Customer has insufficient points balance: " + customer.getTotalPoints());
                }
                customer.setTotalPoints(customer.getTotalPoints() - redeemedPoints);
            }

            // 2. Accrue points & upgrade tier based on final paid amount
            membershipService.accruePointsAndUpgrade(customer, payableAmount);
        }

        // Transition order status to COMPLETED (must go through OrderWorkflowService)
        orderWorkflowService.transitionOrderStatus(orderId, OrderStatus.COMPLETED);

        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public Payment refundPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            return payment;
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        return paymentRepository.save(payment);
    }
}
