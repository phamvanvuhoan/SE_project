package com.restaurant.pos.dto.promotion;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class FixedDiscountRuleDTO extends PromotionRuleDTO {

    @NotNull(message = "Minimum order amount is required")
    @DecimalMin(value = "0.0", message = "Minimum order amount must be 0 or greater")
    private BigDecimal minimumOrderAmount;

    @NotNull(message = "Discount amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount amount must be greater than 0")
    private BigDecimal discountAmount;

    public FixedDiscountRuleDTO() {}

    public BigDecimal getMinimumOrderAmount() { return minimumOrderAmount; }
    public void setMinimumOrderAmount(BigDecimal minimumOrderAmount) { this.minimumOrderAmount = minimumOrderAmount; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
}
