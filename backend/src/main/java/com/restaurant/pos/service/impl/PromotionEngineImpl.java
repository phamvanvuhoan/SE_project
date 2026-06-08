package com.restaurant.pos.service.impl;

import com.restaurant.pos.entity.*;
import com.restaurant.pos.model.AppliedPromotion;
import com.restaurant.pos.model.PromotionResult;
import com.restaurant.pos.service.PromotionEngine;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PromotionEngineImpl implements PromotionEngine {

    @Override
    public PromotionResult applyEventPromotions(Order order, List<EventPromotion> activePromotions) {
        if (order == null || activePromotions == null || activePromotions.isEmpty() || order.getOrderItems().isEmpty()) {
            return new PromotionResult(BigDecimal.ZERO, new ArrayList<>(), new ArrayList<>());
        }

        // Calculate base subtotal of normal items (excluding existing promotional/free items)
        BigDecimal baseSubtotal = BigDecimal.ZERO;
        for (OrderItem item : order.getOrderItems()) {
            if (!item.isPromotionalItem()) {
                baseSubtotal = baseSubtotal.add(item.getSubtotal());
            }
        }

        List<EventPromotion> eligiblePromotions = new ArrayList<>();
        for (EventPromotion promo : activePromotions) {
            if (isPromotionEligible(order, baseSubtotal, promo)) {
                eligiblePromotions.add(promo);
            }
        }

        if (eligiblePromotions.isEmpty()) {
            return new PromotionResult(BigDecimal.ZERO, new ArrayList<>(), new ArrayList<>());
        }

        // Separate stackable and non-stackable promotions
        List<EventPromotion> stackablePromos = new ArrayList<>();
        List<EventPromotion> nonStackablePromos = new ArrayList<>();
        for (EventPromotion promo : eligiblePromotions) {
            if (promo.isStackable()) {
                stackablePromos.add(promo);
            } else {
                nonStackablePromos.add(promo);
            }
        }

        // Scenario A: Evaluate the stackable pool (combine all stackable promotions)
        PromotionResult stackableResult = evaluatePromotionSet(order, baseSubtotal, stackablePromos);

        // Scenario B: Evaluate each non-stackable promotion individually, then pick the
        // option (A or B) that gives the customer the highest total benefit.
        //
        // IMPORTANT: totalDiscount already includes the retail price of complimentary/free
        // items (GiftRule adds giftItem.price; BuyXGetY adds freeItem.price × freeQty).
        // Therefore this comparison IS by total customer benefit, not raw cash discount alone.
        PromotionResult bestResult = stackableResult;
        for (EventPromotion nonStackable : nonStackablePromos) {
            List<EventPromotion> singlePromoList = List.of(nonStackable);
            PromotionResult singleResult = evaluatePromotionSet(order, baseSubtotal, singlePromoList);
            if (singleResult.getTotalDiscount().compareTo(bestResult.getTotalDiscount()) > 0) {
                bestResult = singleResult;
            }
        }

        return bestResult;
    }

    private boolean isPromotionEligible(Order order, BigDecimal baseSubtotal, EventPromotion promo) {
        for (PromotionRule rule : promo.getRules()) {
            if (rule instanceof PercentageDiscountRule) {
                PercentageDiscountRule r = (PercentageDiscountRule) rule;
                if (baseSubtotal.compareTo(r.getMinimumOrderAmount()) < 0) {
                    return false;
                }
            } else if (rule instanceof FixedDiscountRule) {
                FixedDiscountRule r = (FixedDiscountRule) rule;
                if (baseSubtotal.compareTo(r.getMinimumOrderAmount()) < 0) {
                    return false;
                }
            } else if (rule instanceof GiftRule) {
                GiftRule r = (GiftRule) rule;
                if (baseSubtotal.compareTo(r.getMinimumOrderAmount()) < 0) {
                    return false;
                }
            } else if (rule instanceof BuyXGetYRule) {
                BuyXGetYRule r = (BuyXGetYRule) rule;
                boolean hasRequired = false;
                for (OrderItem item : order.getOrderItems()) {
                    if (!item.isPromotionalItem() && item.getMenuItem().getId().equals(r.getRequiredItem().getId())) {
                        if (item.getQuantity() >= r.getRequiredQuantity()) {
                            hasRequired = true;
                            break;
                        }
                    }
                }
                if (!hasRequired) {
                    return false;
                }
            } else if (rule instanceof CategoryDiscountRule) {
                CategoryDiscountRule r = (CategoryDiscountRule) rule;
                boolean hasCategoryItem = false;
                for (OrderItem item : order.getOrderItems()) {
                    if (!item.isPromotionalItem() && item.getMenuItem().getCategory().getId().equals(r.getCategory().getId())) {
                        hasCategoryItem = true;
                        break;
                    }
                }
                if (!hasCategoryItem) {
                    return false;
                }
            }
        }
        return true;
    }

    private PromotionResult evaluatePromotionSet(Order order, BigDecimal baseSubtotal, List<EventPromotion> promos) {
        BigDecimal totalDiscount = BigDecimal.ZERO;
        List<AppliedPromotion> appliedPromotions = new ArrayList<>();
        List<OrderItem> complimentaryItems = new ArrayList<>();

        for (EventPromotion promo : promos) {
            BigDecimal promoDiscount = BigDecimal.ZERO;

            for (PromotionRule rule : promo.getRules()) {
                if (rule instanceof PercentageDiscountRule) {
                    PercentageDiscountRule r = (PercentageDiscountRule) rule;
                    BigDecimal ruleDiscount = baseSubtotal.multiply(r.getDiscountPercentage())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    if (r.getMaximumDiscount() != null && ruleDiscount.compareTo(r.getMaximumDiscount()) > 0) {
                        ruleDiscount = r.getMaximumDiscount();
                    }
                    promoDiscount = promoDiscount.add(ruleDiscount);

                } else if (rule instanceof FixedDiscountRule) {
                    FixedDiscountRule r = (FixedDiscountRule) rule;
                    promoDiscount = promoDiscount.add(r.getDiscountAmount());

                } else if (rule instanceof GiftRule) {
                    GiftRule r = (GiftRule) rule;
                    BigDecimal giftPrice = r.getGiftItem().getPrice();
                    
                    OrderItem giftItem = new OrderItem();
                    giftItem.setId(UUID.randomUUID());
                    giftItem.setOrder(order);
                    giftItem.setMenuItem(r.getGiftItem());
                    giftItem.setQuantity(1);
                    giftItem.setUnitPrice(BigDecimal.ZERO);
                    giftItem.setSubtotal(BigDecimal.ZERO);
                    giftItem.setPromotionalItem(true);
                    
                    complimentaryItems.add(giftItem);
                    promoDiscount = promoDiscount.add(giftPrice);

                } else if (rule instanceof BuyXGetYRule) {
                    BuyXGetYRule r = (BuyXGetYRule) rule;
                    int requiredQty = r.getRequiredQuantity();

                    // Count how many of the required item are in this order
                    int count = 0;
                    for (OrderItem item : order.getOrderItems()) {
                        if (!item.isPromotionalItem() && item.getMenuItem().getId().equals(r.getRequiredItem().getId())) {
                            count += item.getQuantity();
                        }
                    }

                    if (count >= requiredQty) {
                        int multiplier = count / requiredQty;
                        // Apply maxRedemptions cap: 0 means no cap (unlimited multiplier).
                        if (r.getMaxRedemptions() > 0) {
                            multiplier = Math.min(multiplier, r.getMaxRedemptions());
                        }
                        int freeQty = multiplier * r.getFreeQuantity();
                        BigDecimal freeItemPrice = r.getFreeItem().getPrice();

                        OrderItem freeItem = new OrderItem();
                        freeItem.setId(UUID.randomUUID());
                        freeItem.setOrder(order);
                        freeItem.setMenuItem(r.getFreeItem());
                        freeItem.setQuantity(freeQty);
                        freeItem.setUnitPrice(BigDecimal.ZERO);
                        freeItem.setSubtotal(BigDecimal.ZERO);
                        freeItem.setPromotionalItem(true);

                        complimentaryItems.add(freeItem);
                        // Free items' retail value is added to promoDiscount so that
                        // scenario comparison (stackable vs non-stackable) captures total
                        // customer benefit, not cash discount alone.
                        promoDiscount = promoDiscount.add(freeItemPrice.multiply(BigDecimal.valueOf(freeQty)));
                    }

                } else if (rule instanceof CategoryDiscountRule) {
                    CategoryDiscountRule r = (CategoryDiscountRule) rule;
                    BigDecimal categoryDiscount = BigDecimal.ZERO;
                    for (OrderItem item : order.getOrderItems()) {
                        if (!item.isPromotionalItem() && item.getMenuItem().getCategory().getId().equals(r.getCategory().getId())) {
                            BigDecimal itemDiscount = item.getSubtotal().multiply(r.getDiscountPercentage())
                                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                            categoryDiscount = categoryDiscount.add(itemDiscount);
                        }
                    }
                    promoDiscount = promoDiscount.add(categoryDiscount);
                }
            }

            totalDiscount = totalDiscount.add(promoDiscount);
            appliedPromotions.add(new AppliedPromotion(promo.getId(), promo.getName(), promoDiscount));
        }

        return new PromotionResult(totalDiscount, appliedPromotions, complimentaryItems);
    }
}
