package com.restaurant.pos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "buy_x_get_y_rules")
@DiscriminatorValue("BUY_X_GET_Y")
public class BuyXGetYRule extends PromotionRule {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "required_item_id", nullable = false)
    private MenuItem requiredItem;

    @Column(name = "required_quantity", nullable = false)
    private int requiredQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "free_item_id", nullable = false)
    private MenuItem freeItem;

    @Column(name = "free_quantity", nullable = false)
    private int freeQuantity;

    /**
     * Maximum number of times this rule's multiplier may fire on a single order.
     * 0 means no cap (unlimited). E.g. buy2get1 with max_redemptions=2 means a customer
     * ordering 10 of the required item still receives at most 2 free items, not 5.
     */
    @Column(name = "max_redemptions", nullable = false)
    private int maxRedemptions = 0;

    public BuyXGetYRule() {}

    public MenuItem getRequiredItem() { return requiredItem; }
    public void setRequiredItem(MenuItem requiredItem) { this.requiredItem = requiredItem; }

    public int getRequiredQuantity() { return requiredQuantity; }
    public void setRequiredQuantity(int requiredQuantity) { this.requiredQuantity = requiredQuantity; }

    public MenuItem getFreeItem() { return freeItem; }
    public void setFreeItem(MenuItem freeItem) { this.freeItem = freeItem; }

    public int getFreeQuantity() { return freeQuantity; }
    public void setFreeQuantity(int freeQuantity) { this.freeQuantity = freeQuantity; }

    public int getMaxRedemptions() { return maxRedemptions; }
    public void setMaxRedemptions(int maxRedemptions) { this.maxRedemptions = maxRedemptions; }
}
