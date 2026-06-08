package com.restaurant.pos.dto.customer;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class UpdateMembershipLevelRequest {

    @NotBlank(message = "Level name cannot be blank")
    @Size(max = 50, message = "Level name must not exceed 50 characters")
    private String levelName;

    @NotNull(message = "Minimum spend is required")
    @DecimalMin(value = "0.0", message = "Minimum spend must be at least 0")
    private BigDecimal minSpend;

    @NotNull(message = "Point accrual rate is required")
    @DecimalMin(value = "0.0", message = "Point rate must be at least 0")
    @DecimalMax(value = "100.0", message = "Point rate cannot exceed 100")
    private BigDecimal pointRate;

    public UpdateMembershipLevelRequest() {}

    public String getLevelName() { return levelName; }
    public void setLevelName(String levelName) { this.levelName = levelName; }

    public BigDecimal getMinSpend() { return minSpend; }
    public void setMinSpend(BigDecimal minSpend) { this.minSpend = minSpend; }

    public BigDecimal getPointRate() { return pointRate; }
    public void setPointRate(BigDecimal pointRate) { this.pointRate = pointRate; }
}
