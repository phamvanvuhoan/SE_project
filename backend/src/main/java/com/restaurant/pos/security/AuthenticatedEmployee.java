package com.restaurant.pos.security;

import java.util.UUID;

public record AuthenticatedEmployee(
        UUID employeeId,
        String username,
        String role
) {}
