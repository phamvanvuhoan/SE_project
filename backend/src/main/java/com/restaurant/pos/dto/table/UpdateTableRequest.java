package com.restaurant.pos.dto.table;

import com.restaurant.pos.entity.TableStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateTableRequest {

    @NotBlank(message = "Table number cannot be blank")
    @Size(max = 20, message = "Table number must not exceed 20 characters")
    private String tableNumber;

    @NotNull(message = "Seating capacity is required")
    @Min(value = 1, message = "Seating capacity must be at least 1")
    private int seatingCapacity;

    @NotNull(message = "Table status is required")
    private TableStatus status;

    public UpdateTableRequest() {}

    public String getTableNumber() { return tableNumber; }
    public void setTableNumber(String tableNumber) { this.tableNumber = tableNumber; }

    public int getSeatingCapacity() { return seatingCapacity; }
    public void setSeatingCapacity(int seatingCapacity) { this.seatingCapacity = seatingCapacity; }

    public TableStatus getStatus() { return status; }
    public void setStatus(TableStatus status) { this.status = status; }
}
