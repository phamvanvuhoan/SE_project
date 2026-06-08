package com.restaurant.pos.model;

import com.restaurant.pos.entity.OrderItem;
import java.math.BigDecimal;
import java.util.List;

public class OrderCalculationResult {
    private final BigDecimal itemSubtotal;
    private final BigDecimal eventDiscount;
    private final BigDecimal pointRedeemAmount;
    private final BigDecimal finalPayableAmount;
    private final List<OrderItem> calculatedItems;
    private final List<AppliedPromotion> appliedPromotions;

    public OrderCalculationResult(BigDecimal itemSubtotal, BigDecimal eventDiscount, BigDecimal pointRedeemAmount, BigDecimal finalPayableAmount, List<OrderItem> calculatedItems, List<AppliedPromotion> appliedPromotions) {
        this.itemSubtotal = itemSubtotal;
        this.eventDiscount = eventDiscount;
        this.pointRedeemAmount = pointRedeemAmount;
        this.finalPayableAmount = finalPayableAmount;
        this.calculatedItems = calculatedItems;
        this.appliedPromotions = appliedPromotions;
    }

    public BigDecimal getItemSubtotal() {
        return itemSubtotal;
    }

    public BigDecimal getEventDiscount() {
        return eventDiscount;
    }

    public BigDecimal getPointRedeemAmount() {
        return pointRedeemAmount;
    }

    public BigDecimal getFinalPayableAmount() {
        return finalPayableAmount;
    }

    public List<OrderItem> getCalculatedItems() {
        return calculatedItems;
    }

    public List<AppliedPromotion> getAppliedPromotions() {
        return appliedPromotions;
    }
}
