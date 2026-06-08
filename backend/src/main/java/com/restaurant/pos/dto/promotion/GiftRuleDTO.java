package com.restaurant.pos.dto.promotion;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public class GiftRuleDTO extends PromotionRuleDTO {

    @NotNull(message = "Minimum order amount is required")
    @DecimalMin(value = "0.0", message = "Minimum order amount must be 0 or greater")
    private BigDecimal minimumOrderAmount;

    @NotNull(message = "Gift item ID is required")
    private UUID giftItemId;

    private String giftItemName; // Read-only response field

    public GiftRuleDTO() {}

    public BigDecimal getMinimumOrderAmount() { return minimumOrderAmount; }
    public void setMinimumOrderAmount(BigDecimal minimumOrderAmount) { this.minimumOrderAmount = minimumOrderAmount; }

    public UUID getGiftItemId() { return giftItemId; }
    public void setGiftItemId(UUID giftItemId) { this.giftItemId = giftItemId; }

    public String getGiftItemName() { return giftItemName; }
    public void setGiftItemName(String giftItemName) { this.giftItemName = giftItemName; }
}
