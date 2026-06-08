package com.restaurant.pos.service;

import com.restaurant.pos.entity.EventPromotion;
import com.restaurant.pos.entity.Order;
import com.restaurant.pos.model.PromotionResult;
import java.util.List;

public interface PromotionEngine {
    
    /** Evaluates applicable active promotions and returns the best combination result. */
    PromotionResult applyEventPromotions(Order order, List<EventPromotion> activePromotions);
}
