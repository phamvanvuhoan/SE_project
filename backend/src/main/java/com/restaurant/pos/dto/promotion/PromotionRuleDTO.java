package com.restaurant.pos.dto.promotion;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.UUID;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "ruleType"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PercentageDiscountRuleDTO.class, name = "PERCENTAGE_DISCOUNT"),
    @JsonSubTypes.Type(value = FixedDiscountRuleDTO.class, name = "FIXED_DISCOUNT"),
    @JsonSubTypes.Type(value = GiftRuleDTO.class, name = "GIFT"),
    @JsonSubTypes.Type(value = BuyXGetYRuleDTO.class, name = "BUY_X_GET_Y"),
    @JsonSubTypes.Type(value = CategoryDiscountRuleDTO.class, name = "CATEGORY_DISCOUNT")
})
public abstract class PromotionRuleDTO {
    private UUID id;

    public PromotionRuleDTO() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
}
