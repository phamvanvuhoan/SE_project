package com.restaurant.pos.service;

import com.restaurant.pos.entity.Order;
import java.util.UUID;

public interface OrderService {
    
    /** Creates a new order. Enforces BR-01 (at least one item) and BR-04 (only active menu items). */
    Order createOrder(Order order);
    
    /** Adds a menu item to the order and updates persisted totals. */
    Order addItem(UUID orderId, UUID menuItemId, int quantity);
    
    /** Removes an item from the order and updates persisted totals. */
    Order removeItem(UUID orderId, UUID menuItemId);
    
    /** Updates the quantity of an item in the order and updates persisted totals. */
    Order updateQuantity(UUID orderId, UUID menuItemId, int quantity);

    /** Applies customer point redemption to the order, recalculating and persisting the point discount and final amount. */
    Order applyPointsRedemption(UUID orderId, int pointsToRedeem);
}
