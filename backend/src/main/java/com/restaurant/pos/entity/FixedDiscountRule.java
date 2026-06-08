package com.restaurant.pos.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "fixed_discount_rules")
@DiscriminatorValue("FIXED")
public class FixedDiscountRule extends PromotionRule {

    @Column(name = "minimum_order_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal minimumOrderAmount;

    @Column(name = "discount_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal discountAmount;

    public FixedDiscountRule() {}

    public BigDecimal getMinimumOrderAmount() { return minimumOrderAmount; }
    public void setMinimumOrderAmount(BigDecimal minimumOrderAmount) { this.minimumOrderAmount = minimumOrderAmount; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
}
