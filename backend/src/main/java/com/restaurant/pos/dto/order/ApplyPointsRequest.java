package com.restaurant.pos.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ApplyPointsRequest {

    @NotNull(message = "Points value is required")
    @Min(value = 0, message = "Points to redeem must be 0 or greater")
    private Integer pointsToRedeem;

    public ApplyPointsRequest() {}

    public Integer getPointsToRedeem() { return pointsToRedeem; }
    public void setPointsToRedeem(Integer pointsToRedeem) { this.pointsToRedeem = pointsToRedeem; }
}
