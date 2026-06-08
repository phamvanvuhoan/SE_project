package com.restaurant.pos.dto.promotion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class BuyXGetYRuleDTO extends PromotionRuleDTO {

    @NotNull(message = "Required item ID is required")
    private UUID requiredItemId;

    private String requiredItemName; // Read-only response field

    @Min(value = 1, message = "Required quantity must be at least 1")
    private int requiredQuantity;

    @NotNull(message = "Free item ID is required")
    private UUID freeItemId;

    private String freeItemName; // Read-only response field

    @Min(value = 1, message = "Free quantity must be at least 1")
    private int freeQuantity;

    @Min(value = 0, message = "Maximum redemptions cap cannot be negative")
    private int maxRedemptions = 0; // 0 means no cap

    public BuyXGetYRuleDTO() {}

    public UUID getRequiredItemId() { return requiredItemId; }
    public void setRequiredItemId(UUID requiredItemId) { this.requiredItemId = requiredItemId; }

    public String getRequiredItemName() { return requiredItemName; }
    public void setRequiredItemName(String requiredItemName) { this.requiredItemName = requiredItemName; }

    public int getRequiredQuantity() { return requiredQuantity; }
    public void setRequiredQuantity(int requiredQuantity) { this.requiredQuantity = requiredQuantity; }

    public UUID getFreeItemId() { return freeItemId; }
    public void setFreeItemId(UUID freeItemId) { this.freeItemId = freeItemId; }

    public String getFreeItemName() { return freeItemName; }
    public void setFreeItemName(String freeItemName) { this.freeItemName = freeItemName; }

    public int getFreeQuantity() { return freeQuantity; }
    public void setFreeQuantity(int freeQuantity) { this.freeQuantity = freeQuantity; }

    public int getMaxRedemptions() { return maxRedemptions; }
    public void setMaxRedemptions(int maxRedemptions) { this.maxRedemptions = maxRedemptions; }
}
