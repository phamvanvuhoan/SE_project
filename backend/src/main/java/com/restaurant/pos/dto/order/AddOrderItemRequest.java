package com.restaurant.pos.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class AddOrderItemRequest {

    @NotNull(message = "Menu item ID is required")
    private UUID menuItemId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @Size(max = 255, message = "Notes must not exceed 255 characters")
    private String notes;

    public AddOrderItemRequest() {}

    public UUID getMenuItemId() { return menuItemId; }
    public void setMenuItemId(UUID menuItemId) { this.menuItemId = menuItemId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
