package com.restaurant.pos.service.impl;

import com.restaurant.pos.entity.Order;
import com.restaurant.pos.entity.OrderStatus;
import com.restaurant.pos.entity.PaymentStatus;
import com.restaurant.pos.exception.BusinessRuleViolationException;
import com.restaurant.pos.exception.InvalidOrderStateException;
import com.restaurant.pos.exception.ResourceNotFoundException;
import com.restaurant.pos.repository.OrderRepository;
import com.restaurant.pos.repository.PaymentRepository;
import com.restaurant.pos.service.OrderWorkflowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderWorkflowServiceImpl implements OrderWorkflowService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public OrderWorkflowServiceImpl(OrderRepository orderRepository,
                                    PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Transitions an order's status.
     * 
     * <p><b>Propagation note:</b> When called from {@link PaymentService#processPayment},
     * this method joins the existing transaction (PROPAGATION_REQUIRED). It must not be
     * changed to REQUIRES_NEW, as that would break the atomicity of payment completion
     * and order status updates.
     */
    @Override
    @Transactional
    public Order transitionOrderStatus(UUID orderId, OrderStatus targetStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        OrderStatus currentStatus = order.getStatus();
        if (currentStatus == targetStatus) {
            return order;
        }

        validateTransition(orderId, currentStatus, targetStatus);
        order.setStatus(targetStatus);

        return orderRepository.save(order);
    }

    private void validateTransition(UUID orderId, OrderStatus current, OrderStatus target) {
        if (current == OrderStatus.COMPLETED || current == OrderStatus.CANCELLED) {
            throw new InvalidOrderStateException("Cannot transition from terminal state: " + current);
        }

        switch (current) {
            case PENDING:
                if (target != OrderStatus.PREPARING && target != OrderStatus.CANCELLED) {
                    throw new InvalidOrderStateException("Invalid transition from PENDING to " + target);
                }
                break;
            case PREPARING:
                if (target != OrderStatus.READY && target != OrderStatus.CANCELLED) {
                    throw new InvalidOrderStateException("Invalid transition from PREPARING to " + target);
                }
                break;
            case READY:
                if (target != OrderStatus.COMPLETED) {
                    throw new InvalidOrderStateException(
                            "READY orders can only be transitioned to COMPLETED. " +
                            "Call PaymentService.processPayment to complete an order.");
                }
                // Payment gate: READY → COMPLETED is only valid when a COMPLETED payment
                // already exists for this order in the same transaction.  This prevents any
                // caller from bypassing the payment flow and completing an unpaid order.
                boolean hasPaidPayment = paymentRepository.findByOrderId(orderId).stream()
                        .anyMatch(p -> p.getStatus() == PaymentStatus.COMPLETED);
                if (!hasPaidPayment) {
                    throw new BusinessRuleViolationException(
                            "Order " + orderId + " cannot be completed: no COMPLETED payment record found. " +
                            "Process payment via PaymentService.processPayment first.");
                }
                break;
            default:
                throw new InvalidOrderStateException("Unknown current order status: " + current);
        }
    }
}
