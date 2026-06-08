package com.restaurant.pos.model;

import com.restaurant.pos.entity.OrderItem;
import java.math.BigDecimal;
import java.util.List;

public class PromotionResult {
    private final BigDecimal totalDiscount;
    private final List<AppliedPromotion> appliedPromotions;
    private final List<OrderItem> complimentaryItems;

    public PromotionResult(BigDecimal totalDiscount, List<AppliedPromotion> appliedPromotions, List<OrderItem> complimentaryItems) {
        this.totalDiscount = totalDiscount;
        this.appliedPromotions = appliedPromotions;
        this.complimentaryItems = complimentaryItems;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public List<AppliedPromotion> getAppliedPromotions() {
        return appliedPromotions;
    }

    public List<OrderItem> getComplimentaryItems() {
        return complimentaryItems;
    }
}
