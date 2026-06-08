package com.restaurant.pos.service;

import com.restaurant.pos.entity.EventPromotion;
import com.restaurant.pos.entity.Order;
import com.restaurant.pos.model.OrderCalculationResult;
import java.util.List;

public interface OrderCalculationService {
    
    /** Executes the order calculation sequence and returns the calculated results without persisting. */
    OrderCalculationResult calculateOrder(Order order, List<EventPromotion> activePromotions, int pointsToRedeem);
}
