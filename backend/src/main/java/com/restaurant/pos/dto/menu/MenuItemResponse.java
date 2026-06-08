package com.restaurant.pos.dto.menu;

import java.math.BigDecimal;
import java.util.UUID;

public record MenuItemResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        UUID categoryId,
        String categoryName,
        boolean isActive
) {}
