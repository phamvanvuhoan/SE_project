package com.restaurant.pos.dto.employee;

import com.restaurant.pos.entity.EmployeeRole;
import java.util.UUID;

public record EmployeeResponse(
        UUID id,
        String name,
        EmployeeRole role,
        String phone,
        String username,
        boolean isActive
) {}
