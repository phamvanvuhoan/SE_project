package com.restaurant.pos.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class UpdateOrderItemRequest {

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @Size(max = 255, message = "Notes must not exceed 255 characters")
    private String notes;

    public UpdateOrderItemRequest() {}

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
