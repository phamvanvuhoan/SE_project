package com.restaurant.pos.entity;

import jakarta.persistence.*;
import java.util.UUID;
import java.util.Objects;

@Entity
@Table(name = "promotion_rules")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "rule_type", discriminatorType = DiscriminatorType.STRING)
public abstract class PromotionRule {

    @Id
    @Column(name = "rule_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventPromotion eventPromotion;

    /** Persisted via @DiscriminatorColumn — readable via subclass annotations. */
    @Column(name = "rule_type", length = 50, nullable = false, insertable = false, updatable = false)
    private String ruleType;

    /** Optimistic locking: prevents concurrent manager updates from silently overwriting
     *  rule parameters (e.g. discount_percentage, minimum_order_amount). */
    @Version
    @Column(name = "version", nullable = false)
    private int version = 0;

    public PromotionRule() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public EventPromotion getEventPromotion() { return eventPromotion; }
    public void setEventPromotion(EventPromotion eventPromotion) { this.eventPromotion = eventPromotion; }

    public String getRuleType() { return ruleType; }

    public int getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PromotionRule that = (PromotionRule) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
