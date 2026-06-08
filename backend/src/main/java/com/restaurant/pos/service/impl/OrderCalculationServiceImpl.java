package com.restaurant.pos.service.impl;

import com.restaurant.pos.entity.EventPromotion;
import com.restaurant.pos.entity.Order;
import com.restaurant.pos.entity.OrderItem;
import com.restaurant.pos.model.OrderCalculationResult;
import com.restaurant.pos.model.PromotionResult;
import com.restaurant.pos.service.MembershipService;
import com.restaurant.pos.service.OrderCalculationService;
import com.restaurant.pos.service.PromotionEngine;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderCalculationServiceImpl implements OrderCalculationService {

    private final PromotionEngine promotionEngine;
    private final MembershipService membershipService;

    public OrderCalculationServiceImpl(PromotionEngine promotionEngine, MembershipService membershipService) {
        this.promotionEngine = promotionEngine;
        this.membershipService = membershipService;
    }

    @Override
    public OrderCalculationResult calculateOrder(Order order, List<EventPromotion> activePromotions, int pointsToRedeem) {
        if (order == null) {
            return new OrderCalculationResult(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, new ArrayList<>(), new ArrayList<>());
        }

        // 1. Calculate raw item subtotal (normal items only)
        BigDecimal normalItemsSum = BigDecimal.ZERO;
        List<OrderItem> normalItems = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            if (!item.isPromotionalItem()) {
                normalItemsSum = normalItemsSum.add(item.getSubtotal());
                normalItems.add(item);
            }
        }

        // 2. Evaluate promotions
        PromotionResult promoResult = promotionEngine.applyEventPromotions(order, activePromotions);

        // 3. Compute normal value of complimentary items
        BigDecimal freeItemsNormalValue = BigDecimal.ZERO;
        for (OrderItem compItem : promoResult.getComplimentaryItems()) {
            freeItemsNormalValue = freeItemsNormalValue.add(
                    compItem.getMenuItem().getPrice().multiply(BigDecimal.valueOf(compItem.getQuantity()))
            );
        }

        // Subtotal = normal items subtotal + normal value of free items
        BigDecimal subtotal = normalItemsSum.add(freeItemsNormalValue).setScale(2, RoundingMode.HALF_UP);
        BigDecimal eventDiscount = promoResult.getTotalDiscount().setScale(2, RoundingMode.HALF_UP);

        // Intermediate total after promotions
        BigDecimal intermediateTotal = subtotal.subtract(eventDiscount);
        if (intermediateTotal.compareTo(BigDecimal.ZERO) < 0) {
            intermediateTotal = BigDecimal.ZERO;
        }

        // 4. Calculate point redemption
        BigDecimal pointRedeemAmount = BigDecimal.ZERO;
        if (order.getCustomer() != null && pointsToRedeem > 0) {
            BigDecimal maxRedemption = membershipService.calculateMaxPointsRedemption(order.getCustomer(), intermediateTotal);
            BigDecimal requestedRedemption = BigDecimal.valueOf(pointsToRedeem);
            pointRedeemAmount = maxRedemption.min(requestedRedemption).setScale(2, RoundingMode.HALF_UP);
        }

        // 5. Final payable amount
        BigDecimal finalPayableAmount = intermediateTotal.subtract(pointRedeemAmount).setScale(2, RoundingMode.HALF_UP);

        // Assemble calculated items (normal items + complimentary items)
        List<OrderItem> calculatedItems = new ArrayList<>(normalItems);
        calculatedItems.addAll(promoResult.getComplimentaryItems());

        return new OrderCalculationResult(
                subtotal,
                eventDiscount,
                pointRedeemAmount,
                finalPayableAmount,
                calculatedItems,
                promoResult.getAppliedPromotions()
        );
    }
}
