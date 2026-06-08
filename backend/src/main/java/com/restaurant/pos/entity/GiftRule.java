package com.restaurant.pos.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "gift_rules")
@DiscriminatorValue("GIFT")
public class GiftRule extends PromotionRule {

    @Column(name = "minimum_order_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal minimumOrderAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_item_id", nullable = false)
    private MenuItem giftItem;

    public GiftRule() {}

    public BigDecimal getMinimumOrderAmount() { return minimumOrderAmount; }
    public void setMinimumOrderAmount(BigDecimal minimumOrderAmount) { this.minimumOrderAmount = minimumOrderAmount; }

    public MenuItem getGiftItem() { return giftItem; }
    public void setGiftItem(MenuItem giftItem) { this.giftItem = giftItem; }
}
