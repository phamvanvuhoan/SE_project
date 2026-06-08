package com.restaurant.pos.model;

import java.math.BigDecimal;
import java.util.UUID;

public class AppliedPromotion {
    private final UUID eventId;
    private final String name;
    private final BigDecimal discountAmount;

    public AppliedPromotion(UUID eventId, String name, BigDecimal discountAmount) {
        this.eventId = eventId;
        this.name = name;
        this.discountAmount = discountAmount;
    }

    public UUID getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
}
