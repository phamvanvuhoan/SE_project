package com.restaurant.pos.service.impl;

import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.order.AddOrderItemRequest;
import com.restaurant.pos.dto.order.ApplyPointsRequest;
import com.restaurant.pos.dto.order.CreateOrderRequest;
import com.restaurant.pos.dto.order.OrderItemResponse;
import com.restaurant.pos.dto.order.OrderResponse;
import com.restaurant.pos.dto.order.OrderStatusTransitionRequest;
import com.restaurant.pos.dto.order.UpdateOrderItemRequest;
import com.restaurant.pos.dto.payment.PaymentResponse;
import com.restaurant.pos.dto.payment.ProcessPaymentRequest;
import com.restaurant.pos.entity.Customer;
import com.restaurant.pos.entity.Employee;
import com.restaurant.pos.entity.Order;
import com.restaurant.pos.entity.OrderItem;
import com.restaurant.pos.entity.OrderStatus;
import com.restaurant.pos.entity.RestaurantTable;
import com.restaurant.pos.exception.BusinessRuleViolationException;
import com.restaurant.pos.exception.ResourceNotFoundException;
import com.restaurant.pos.repository.CustomerRepository;
import com.restaurant.pos.repository.EmployeeRepository;
import com.restaurant.pos.repository.OrderItemRepository;
import com.restaurant.pos.repository.OrderRepository;
import com.restaurant.pos.repository.RestaurantTableRepository;
import com.restaurant.pos.service.OrderCrudService;
import com.restaurant.pos.service.OrderService;
import com.restaurant.pos.service.OrderWorkflowService;
import com.restaurant.pos.service.PageResponseFactory;
import com.restaurant.pos.service.PaymentService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class OrderCrudServiceImpl implements OrderCrudService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final RestaurantTableRepository tableRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final OrderService orderService;
    private final OrderWorkflowService orderWorkflowService;
    private final PaymentService paymentService;

    public OrderCrudServiceImpl(OrderRepository orderRepository,
                                OrderItemRepository orderItemRepository,
                                RestaurantTableRepository tableRepository,
                                EmployeeRepository employeeRepository,
                                CustomerRepository customerRepository,
                                OrderService orderService,
                                OrderWorkflowService orderWorkflowService,
                                PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.tableRepository = tableRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.orderService = orderService;
        this.orderWorkflowService = orderWorkflowService;
        this.paymentService = paymentService;
    }

    @Override
    public PagedResponse<OrderResponse> findAll(OrderStatus status, Pageable pageable) {
        if (status == null) {
            return PageResponseFactory.fromPage(orderRepository.findAll(pageable), this::toResponse);
        }
        return PageResponseFactory.fromPage(orderRepository.findByStatus(status, pageable), this::toResponse);
    }

    @Override
    public OrderResponse findById(UUID id) {
        return toResponse(findOrder(id));
    }

    @Override
    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        RestaurantTable table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + request.getTableId()));
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));
        if (!employee.isActive()) {
            throw new BusinessRuleViolationException("Inactive employees cannot place orders.");
        }
        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));
        }
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setTable(table);
        order.setEmployee(employee);
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderTime(LocalDateTime.now());
        order.setOrderItems(new ArrayList<>());
        return toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponse addItem(UUID id, AddOrderItemRequest request) {
        return toResponse(orderService.addItem(id, request.getMenuItemId(), request.getQuantity()));
    }

    @Override
    @Transactional
    public OrderResponse updateItem(UUID id, UUID itemId, UpdateOrderItemRequest request) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + itemId));
        if (!item.getOrder().getId().equals(id)) {
            throw new ResourceNotFoundException("Order item not found in order.");
        }
        return toResponse(orderService.updateQuantity(id, item.getMenuItem().getId(), request.getQuantity()));
    }

    @Override
    @Transactional
    public OrderResponse removeItem(UUID id, UUID itemId) {
        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + itemId));
        if (!item.getOrder().getId().equals(id)) {
            throw new ResourceNotFoundException("Order item not found in order.");
        }
        return toResponse(orderService.removeItem(id, item.getMenuItem().getId()));
    }

    @Override
    @Transactional
    public OrderResponse applyPoints(UUID id, ApplyPointsRequest request) {
        return toResponse(orderService.applyPointsRedemption(id, request.getPointsToRedeem()));
    }

    @Override
    @Transactional
    public OrderResponse transition(UUID id, OrderStatusTransitionRequest request) {
        return toResponse(orderWorkflowService.transitionOrderStatus(id, request.getTargetStatus()));
    }

    @Override
    @Transactional
    public PaymentResponse processPayment(UUID id, ProcessPaymentRequest request) {
        return PaymentCrudServiceImpl.toResponseStatic(
                paymentService.processPayment(id, request.getPaymentMethod(), request.getAmount()));
    }

    private Order findOrder(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTable() == null ? null : order.getTable().getId(),
                order.getTable() == null ? null : order.getTable().getTableNumber(),
                order.getEmployee() == null ? null : order.getEmployee().getId(),
                order.getEmployee() == null ? null : order.getEmployee().getName(),
                order.getCustomer() == null ? null : order.getCustomer().getId(),
                order.getCustomer() == null ? null : order.getCustomer().getName(),
                order.getStatus(),
                zeroIfNull(order.getSubtotal()),
                zeroIfNull(order.getPromotionDiscount()),
                zeroIfNull(order.getPointDiscount()),
                zeroIfNull(order.getTotalAmount()),
                order.getOrderTime(),
                order.getOrderItems().stream().map(this::toItemResponse).toList()
        );
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getMenuItem() == null ? null : item.getMenuItem().getId(),
                item.getMenuItem() == null ? null : item.getMenuItem().getDishName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal(),
                null,
                item.isPromotionalItem()
        );
    }

    private BigDecimal zeroIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
