package com.restaurant.pos.dto.table;

import com.restaurant.pos.entity.TableStatus;
import java.util.UUID;

public record TableResponse(
        UUID id,
        String tableNumber,
        int seatingCapacity,
        TableStatus status
) {}
