package com.restaurant.pos.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.Objects;

@Entity
@Table(name = "membership_levels")
public class MembershipLevel {

    @Id
    @Column(name = "membership_id")
    private UUID id;

    @Column(name = "level_name", length = 20, unique = true, nullable = false)
    private String levelName;

    @Column(name = "min_spend", precision = 12, scale = 2, nullable = false)
    private BigDecimal minSpend;

    @Column(name = "point_rate", precision = 5, scale = 2, nullable = false)
    private BigDecimal pointRate;

    @Column(name = "benefit_description", columnDefinition = "TEXT")
    private String benefitDescription;

    public MembershipLevel() {}

    public MembershipLevel(UUID id, String levelName, BigDecimal minSpend, BigDecimal pointRate, String benefitDescription) {
        this.id = id;
        this.levelName = levelName;
        this.minSpend = minSpend;
        this.pointRate = pointRate;
        this.benefitDescription = benefitDescription;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public BigDecimal getMinSpend() {
        return minSpend;
    }

    public void setMinSpend(BigDecimal minSpend) {
        this.minSpend = minSpend;
    }

    public BigDecimal getPointRate() {
        return pointRate;
    }

    public void setPointRate(BigDecimal pointRate) {
        this.pointRate = pointRate;
    }

    public String getBenefitDescription() {
        return benefitDescription;
    }

    public void setBenefitDescription(String benefitDescription) {
        this.benefitDescription = benefitDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MembershipLevel that = (MembershipLevel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
