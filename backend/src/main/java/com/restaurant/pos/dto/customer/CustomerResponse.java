package com.restaurant.pos.dto.customer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String name,
        String phone,
        String email,
        int totalPoints,
        BigDecimal totalSpent,
        String membershipLevelName,
        LocalDateTime createdAt
) {}
