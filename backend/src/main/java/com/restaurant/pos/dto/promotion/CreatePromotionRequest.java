package com.restaurant.pos.dto.promotion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CreatePromotionRequest {

    @NotBlank(message = "Promotion name cannot be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    private boolean isActive = true;

    private boolean isStackable = false;

    @NotNull(message = "Created by employee ID is required")
    private UUID createdById;

    @Valid
    @NotNull(message = "At least one promotion rule is required")
    @Size(min = 1, message = "Promotion must contain at least one rule")
    private List<PromotionRuleDTO> rules;

    public CreatePromotionRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public boolean isStackable() { return isStackable; }
    public void setStackable(boolean stackable) { isStackable = stackable; }

    public UUID getCreatedById() { return createdById; }
    public void setCreatedById(UUID createdById) { this.createdById = createdById; }

    public List<PromotionRuleDTO> getRules() { return rules; }
    public void setRules(List<PromotionRuleDTO> rules) { this.rules = rules; }
}
