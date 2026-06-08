package com.restaurant.pos.service.impl;

import com.restaurant.pos.entity.Order;
import com.restaurant.pos.entity.OrderStatus;
import com.restaurant.pos.exception.InvalidOrderStateException;
import com.restaurant.pos.exception.ResourceNotFoundException;
import com.restaurant.pos.repository.OrderRepository;
import com.restaurant.pos.service.OrderWorkflowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderWorkflowServiceImpl implements OrderWorkflowService {

    private final OrderRepository orderRepository;

    public OrderWorkflowServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public Order transitionOrderStatus(UUID orderId, OrderStatus targetStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        OrderStatus currentStatus = order.getStatus();
        if (currentStatus == targetStatus) {
            return order;
        }

        validateTransition(currentStatus, targetStatus);
        order.setStatus(targetStatus);
        
        return orderRepository.save(order);
    }

    private void validateTransition(OrderStatus current, OrderStatus target) {
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
                    throw new InvalidOrderStateException("Invalid transition from READY to " + target);
                }
                break;
            default:
                throw new InvalidOrderStateException("Unknown current order status: " + current);
        }
    }
}
