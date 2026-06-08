package com.restaurant.pos.dto.table;

import com.restaurant.pos.entity.TableStatus;
import jakarta.validation.constraints.NotNull;

public class TableStatusUpdateRequest {

    @NotNull(message = "Table status is required")
    private TableStatus status;

    public TableStatusUpdateRequest() {}

    public TableStatus getStatus() { return status; }
    public void setStatus(TableStatus status) { this.status = status; }
}
