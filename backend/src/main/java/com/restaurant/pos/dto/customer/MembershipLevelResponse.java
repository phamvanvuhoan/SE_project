package com.restaurant.pos.dto.customer;

import java.math.BigDecimal;
import java.util.UUID;

public record MembershipLevelResponse(
        UUID id,
        String levelName,
        BigDecimal minSpend,
        BigDecimal pointRate
) {}
