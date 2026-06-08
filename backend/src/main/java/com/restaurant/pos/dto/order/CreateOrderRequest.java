package com.restaurant.pos.dto.order;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class CreateOrderRequest {

    @NotNull(message = "Table ID is required")
    private UUID tableId;

    @NotNull(message = "Employee ID is required")
    private UUID employeeId;

    private UUID customerId; // Optional loyalty account

    public CreateOrderRequest() {}

    public UUID getTableId() { return tableId; }
    public void setTableId(UUID tableId) { this.tableId = tableId; }

    public UUID getEmployeeId() { return employeeId; }
    public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }

    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }
}
