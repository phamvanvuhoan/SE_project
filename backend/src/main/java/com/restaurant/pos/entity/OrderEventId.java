package com.restaurant.pos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;
import java.util.Objects;

@Embeddable
public class OrderEventId implements Serializable {

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "event_id")
    private UUID eventId;

    public OrderEventId() {}

    public OrderEventId(UUID orderId, UUID eventId) {
        this.orderId = orderId;
        this.eventId = eventId;
    }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEventId that = (OrderEventId) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() { return Objects.hash(orderId, eventId); }
}
