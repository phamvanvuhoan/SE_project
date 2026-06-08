package com.restaurant.pos.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_events")
public class OrderEvent {

    @EmbeddedId
    private OrderEventId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    private EventPromotion eventPromotion;

    @Column(name = "discount_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    public OrderEvent() {}

    public OrderEvent(Order order, EventPromotion eventPromotion, BigDecimal discountAmount) {
        this.id = new OrderEventId(order.getId(), eventPromotion.getId());
        this.order = order;
        this.eventPromotion = eventPromotion;
        this.discountAmount = discountAmount;
    }

    public OrderEventId getId() { return id; }
    public void setId(OrderEventId id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public EventPromotion getEventPromotion() { return eventPromotion; }
    public void setEventPromotion(EventPromotion eventPromotion) { this.eventPromotion = eventPromotion; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEvent that = (OrderEvent) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
