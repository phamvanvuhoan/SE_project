package com.restaurant.pos.dto.menu;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        String description
) {}
