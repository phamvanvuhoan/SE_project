package com.restaurant.pos.dto.promotion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PromotionResponse(
        UUID id,
        String name,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        boolean isActive,
        boolean isStackable,
        UUID createdById,
        String createdByName,
        List<PromotionRuleDTO> rules
) {}
