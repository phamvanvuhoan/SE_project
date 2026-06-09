package com.restaurant.pos.service.impl;

import com.restaurant.pos.entity.Employee;
import com.restaurant.pos.entity.EventPromotion;
import com.restaurant.pos.entity.MenuItem;
import com.restaurant.pos.entity.Order;
import com.restaurant.pos.entity.OrderEvent;
import com.restaurant.pos.entity.OrderEventId;
import com.restaurant.pos.entity.OrderItem;
import com.restaurant.pos.entity.OrderStatus;
import com.restaurant.pos.exception.BusinessRuleViolationException;
import com.restaurant.pos.exception.InvalidOrderStateException;
import com.restaurant.pos.exception.ResourceNotFoundException;
import com.restaurant.pos.model.AppliedPromotion;
import com.restaurant.pos.model.OrderCalculationResult;
import com.restaurant.pos.repository.EmployeeRepository;
import com.restaurant.pos.repository.EventPromotionRepository;
import com.restaurant.pos.repository.MenuItemRepository;
import com.restaurant.pos.repository.OrderEventRepository;
import com.restaurant.pos.repository.OrderRepository;
import com.restaurant.pos.service.OrderCalculationService;
import com.restaurant.pos.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final EmployeeRepository employeeRepository;
    private final EventPromotionRepository eventPromotionRepository;
    private final OrderEventRepository orderEventRepository;
    private final OrderCalculationService orderCalculationService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            MenuItemRepository menuItemRepository,
                            EmployeeRepository employeeRepository,
                            EventPromotionRepository eventPromotionRepository,
                            OrderEventRepository orderEventRepository,
                            OrderCalculationService orderCalculationService) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.employeeRepository = employeeRepository;
        this.eventPromotionRepository = eventPromotionRepository;
        this.orderEventRepository = orderEventRepository;
        this.orderCalculationService = orderCalculationService;
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        // Enforce BR-01: Order must contain at least one item
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new BusinessRuleViolationException("Order must contain at least one item.");
        }

        // Validate Employee
        Employee employee = employeeRepository.findById(order.getEmployee().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found."));
        if (!employee.isActive()) {
            throw new BusinessRuleViolationException("Inactive employees cannot place orders.");
        }
        order.setEmployee(employee);

        // Validate and attach Menu Items (Enforce BR-04)
        for (OrderItem item : order.getOrderItems()) {
            MenuItem menuItem = menuItemRepository.findById(item.getMenuItem().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + item.getMenuItem().getId()));
            if (!menuItem.isAvailable()) {
                throw new BusinessRuleViolationException("Menu item is not available: " + menuItem.getDishName());
            }
            item.setMenuItem(menuItem);
            item.setOrder(order);
            if (item.getId() == null) {
                item.setId(UUID.randomUUID());
            }
            item.setUnitPrice(menuItem.getPrice());
            item.setSubtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            item.setPromotionalItem(false);
        }

        order.setStatus(OrderStatus.PENDING);
        order.setOrderTime(LocalDateTime.now());

        // Perform calculation with active promotions and 0 points redemption initially
        List<EventPromotion> activePromos = eventPromotionRepository.findCurrentlyActive(LocalDateTime.now());
        OrderCalculationResult calcResult = orderCalculationService.calculateOrder(order, activePromos, 0);

        applyCalculationResult(order, calcResult);

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order addItem(UUID orderId, UUID menuItemId, int quantity) {
        Order order = fetchAndValidatePendingOrder(orderId);

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found."));
        if (!menuItem.isAvailable()) {
            throw new BusinessRuleViolationException("Menu item is not available: " + menuItem.getDishName());
        }

        // Remove existing promotional items before recalculating
        order.getOrderItems().removeIf(OrderItem::isPromotionalItem);

        OrderItem existingItem = null;
        for (OrderItem item : order.getOrderItems()) {
            if (item.getMenuItem().getId().equals(menuItemId)) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setSubtotal(existingItem.getUnitPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity())));
        } else {
            OrderItem newItem = new OrderItem();
            newItem.setId(UUID.randomUUID());
            newItem.setOrder(order);
            newItem.setMenuItem(menuItem);
            newItem.setQuantity(quantity);
            newItem.setUnitPrice(menuItem.getPrice());
            newItem.setSubtotal(menuItem.getPrice().multiply(BigDecimal.valueOf(quantity)));
            newItem.setPromotionalItem(false);
            order.getOrderItems().add(newItem);
        }

        recalculateAndSave(order);
        return order;
    }

    @Override
    @Transactional
    public Order removeItem(UUID orderId, UUID menuItemId) {
        Order order = fetchAndValidatePendingOrder(orderId);

        order.getOrderItems().removeIf(OrderItem::isPromotionalItem);
        boolean removed = order.getOrderItems().removeIf(item -> item.getMenuItem().getId().equals(menuItemId));
        if (!removed) {
            throw new ResourceNotFoundException("Item not found in order.");
        }

        if (order.getOrderItems().isEmpty()) {
            throw new BusinessRuleViolationException("Cannot remove the last item. Order must have at least one item.");
        }

        recalculateAndSave(order);
        return order;
    }

    @Override
    @Transactional
    public Order updateQuantity(UUID orderId, UUID menuItemId, int quantity) {
        Order order = fetchAndValidatePendingOrder(orderId);

        if (quantity <= 0) {
            return removeItem(orderId, menuItemId);
        }

        order.getOrderItems().removeIf(OrderItem::isPromotionalItem);

        OrderItem targetItem = null;
        for (OrderItem item : order.getOrderItems()) {
            if (item.getMenuItem().getId().equals(menuItemId)) {
                targetItem = item;
                break;
            }
        }

        if (targetItem == null) {
            throw new ResourceNotFoundException("Item not found in order.");
        }

        targetItem.setQuantity(quantity);
        targetItem.setSubtotal(targetItem.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));

        recalculateAndSave(order);
        return order;
    }

    private Order fetchAndValidatePendingOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStateException("Order modifications are only allowed in PENDING status.");
        }
        return order;
    }

    private void recalculateAndSave(Order order) {
        List<EventPromotion> activePromos = eventPromotionRepository.findCurrentlyActive(LocalDateTime.now());
        // Since we are adding/updating items, we calculate without points redemption
        OrderCalculationResult calcResult = orderCalculationService.calculateOrder(order, activePromos, 0);
        applyCalculationResult(order, calcResult);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order applyPointsRedemption(UUID orderId, int pointsToRedeem) {
        Order order = fetchAndValidatePendingOrder(orderId);
        if (order.getCustomer() == null) {
            throw new BusinessRuleViolationException("Cannot redeem points: order has no associated customer.");
        }

        List<EventPromotion> activePromos = eventPromotionRepository.findCurrentlyActive(LocalDateTime.now());
        OrderCalculationResult calcResult = orderCalculationService.calculateOrder(order, activePromos, pointsToRedeem);
        applyCalculationResult(order, calcResult);

        return orderRepository.save(order);
    }

    private void applyCalculationResult(Order order, OrderCalculationResult calcResult) {
        order.updateTotals(
                calcResult.getItemSubtotal(),
                calcResult.getEventDiscount(),
                calcResult.getPointRedeemAmount(),
                calcResult.getFinalPayableAmount()
        );

        // Manage complimentary items via the in-memory collection (owned, orphanRemoval=true)
        order.getOrderItems().removeIf(OrderItem::isPromotionalItem);
        for (OrderItem calcItem : calcResult.getCalculatedItems()) {
            if (calcItem.isPromotionalItem()) {
                order.getOrderItems().add(calcItem);
            }
        }

        // Persist OrderEvents via repository directly.
        // OrderEvent collection no longer has orphanRemoval=true, so we manage
        // deletions explicitly to avoid the DELETE+re-INSERT hazard on every recalculation.
        if (order.getId() != null) {
            orderEventRepository.deleteByOrderId(order.getId());
        }
        List<OrderEvent> orderEvents = new ArrayList<>();
        if (calcResult.getAppliedPromotions() != null) {
            for (AppliedPromotion ap : calcResult.getAppliedPromotions()) {
                OrderEvent oe = new OrderEvent(
                        order,
                        eventPromotionRepository.getReferenceById(ap.getEventId()),
                        ap.getDiscountAmount()
                );
                orderEvents.add(oe);
            }
        }
        orderEventRepository.saveAll(orderEvents);
    }
}
