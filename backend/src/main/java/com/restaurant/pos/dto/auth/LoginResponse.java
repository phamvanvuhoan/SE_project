package com.restaurant.pos.dto.auth;

import java.util.UUID;

public record LoginResponse(
        UUID employeeId,
        String name,
        String role,
        String token
) {}
