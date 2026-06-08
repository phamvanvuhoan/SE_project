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
import java.math.RoundingMode;
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

    /**
     * Processes a payment for a READY order.
     *
     * <p><b>Propagation note:</b> {@code accruePointsAndUpgrade} is {@code @Transactional}
     * with default {@code PROPAGATION_REQUIRED}, meaning it joins this outer transaction.
     * Any failure after the membership save (e.g. in {@code paymentRepository.save} or
     * {@code transitionOrderStatus}) will roll back the entire transaction including the
     * customer update. If {@code accruePointsAndUpgrade} is ever changed to
     * {@code REQUIRES_NEW}, that atomicity guarantee is broken — coordinate with this method.
     */
    @Override
    @Transactional
    public Payment processPayment(UUID orderId, PaymentMethod method, BigDecimal amount) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (order.getStatus() != OrderStatus.READY) {
            throw new InvalidOrderStateException(
                    "Order must be in READY status to complete payment. Current status: " + order.getStatus());
        }

        // Use persisted order amount — never recalculate here
        BigDecimal payableAmount = order.getTotalAmount();
        if (amount.compareTo(payableAmount) < 0) {
            throw new BusinessRuleViolationException(
                    "Paid amount is less than the payable order amount: " + payableAmount);
        }

        // Build payment record
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setOrder(order);
        payment.setPaymentMethod(method);
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaidAt(LocalDateTime.now());

        // Customer loyalty updates
        Customer customer = order.getCustomer();
        if (customer != null) {
            int redeemedPoints = order.getPointDiscount().setScale(0, RoundingMode.HALF_UP).intValue();

            // 1. Validate and deduct redeemed points
            if (redeemedPoints > 0) {
                if (customer.getTotalPoints() < redeemedPoints) {
                    throw new BusinessRuleViolationException(
                            "Customer has insufficient points balance: " + customer.getTotalPoints()
                            + " (required: " + redeemedPoints + ")");
                }
                customer.setTotalPoints(customer.getTotalPoints() - redeemedPoints);
            }

            // 2. Accrue earned points & evaluate tier upgrade.
            //    grossValueForTier = subtotal - promotionDiscount (economic value consumed).
            //    cashPaid = totalAmount (cash that actually changed hands, post-point-redemption).
            //    See MembershipService.accruePointsAndUpgrade for the explicit design decision.
            BigDecimal grossValueForTier = order.getSubtotal().subtract(order.getPromotionDiscount());
            membershipService.accruePointsAndUpgrade(customer, grossValueForTier, payableAmount);
        }

        Payment savedPayment = paymentRepository.save(payment);

        // Transition order status to COMPLETED — must go through OrderWorkflowService.
        // OrderWorkflowService guards this transition so it cannot be invoked without a
        // verified COMPLETED payment in the same transaction.
        orderWorkflowService.transitionOrderStatus(orderId, OrderStatus.COMPLETED);

        return savedPayment;
    }

    /**
     * Refunds a payment and reverses all loyalty side-effects: earned points deducted,
     * redeemed points restored, total_spent reduced, tier re-evaluated.
     */
    @Override
    @Transactional
    public Payment refundPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));

        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            return payment;
        }

        Order order = payment.getOrder();
        Customer customer = order.getCustomer();

        if (customer != null) {
            // Reverse the loyalty state that was applied at processPayment time.
            // Re-derive values using the same formulas used during payment to ensure symmetry.
            BigDecimal grossValueForTier = order.getSubtotal().subtract(order.getPromotionDiscount());
            int redeemedPoints = order.getPointDiscount().setScale(0, RoundingMode.HALF_UP).intValue();

            // Earned points were calculated on cashPaid (totalAmount) at payment time.
            // Re-derive pointRate from the customer's CURRENT membership level (it may have
            // upgraded as a result of this payment, so this is a best-effort reversal).
            BigDecimal pointRate = BigDecimal.ZERO;
            if (customer.getMembershipLevel() != null) {
                pointRate = customer.getMembershipLevel().getPointRate();
            }
            int earnedPoints = order.getTotalAmount()
                    .multiply(pointRate)
                    .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP)
                    .intValue();

            membershipService.reversePointsForRefund(customer, earnedPoints, redeemedPoints, grossValueForTier);
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        return paymentRepository.save(payment);
    }
}
