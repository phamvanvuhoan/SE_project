package com.restaurant.pos.dto.promotion;

import jakarta.validation.constraints.NotNull;

public class PromotionStatusUpdateRequest {

    @NotNull(message = "Active flag is required")
    private Boolean active;

    public PromotionStatusUpdateRequest() {}

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
