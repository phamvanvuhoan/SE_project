package com.restaurant.pos.dto.promotion;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PercentageDiscountRuleDTO extends PromotionRuleDTO {

    @NotNull(message = "Minimum order amount is required")
    @DecimalMin(value = "0.0", message = "Minimum order amount must be 0 or greater")
    private BigDecimal minimumOrderAmount;

    @NotNull(message = "Discount percentage is required")
    @DecimalMin(value = "0.0", message = "Discount percentage must be 0 or greater")
    @DecimalMax(value = "100.0", message = "Discount percentage cannot exceed 100")
    private BigDecimal discountPercentage;

    @DecimalMin(value = "0.0", message = "Maximum discount must be 0 or greater")
    private BigDecimal maximumDiscount;

    public PercentageDiscountRuleDTO() {}

    public BigDecimal getMinimumOrderAmount() { return minimumOrderAmount; }
    public void setMinimumOrderAmount(BigDecimal minimumOrderAmount) { this.minimumOrderAmount = minimumOrderAmount; }

    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }

    public BigDecimal getMaximumDiscount() { return maximumDiscount; }
    public void setMaximumDiscount(BigDecimal maximumDiscount) { this.maximumDiscount = maximumDiscount; }
}
