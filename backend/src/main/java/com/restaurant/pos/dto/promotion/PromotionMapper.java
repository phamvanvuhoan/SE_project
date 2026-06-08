package com.restaurant.pos.dto.promotion;

import com.restaurant.pos.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PromotionMapper {

    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "createdByName", source = "createdBy.name")
    @Mapping(target = "rules", expression = "java(mapRulesToDTO(promotion.getRules()))")
    PromotionResponse toResponse(EventPromotion promotion);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "rules", expression = "java(mapDTOsToRules(request.getRules()))")
    EventPromotion toEntity(CreatePromotionRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "rules", expression = "java(mapDTOsToRules(request.getRules()))")
    EventPromotion toEntity(UpdatePromotionRequest request);

    default List<PromotionRuleDTO> mapRulesToDTO(List<PromotionRule> rules) {
        if (rules == null) return null;
        return rules.stream().map(rule -> {
            if (rule instanceof PercentageDiscountRule r) {
                PercentageDiscountRuleDTO dto = new PercentageDiscountRuleDTO();
                dto.setId(r.getId());
                dto.setMinimumOrderAmount(r.getMinimumOrderAmount());
                dto.setDiscountPercentage(r.getDiscountPercentage());
                dto.setMaximumDiscount(r.getMaximumDiscount());
                return dto;
            } else if (rule instanceof FixedDiscountRule r) {
                FixedDiscountRuleDTO dto = new FixedDiscountRuleDTO();
                dto.setId(r.getId());
                dto.setMinimumOrderAmount(r.getMinimumOrderAmount());
                dto.setDiscountAmount(r.getDiscountAmount());
                return dto;
            } else if (rule instanceof GiftRule r) {
                GiftRuleDTO dto = new GiftRuleDTO();
                dto.setId(r.getId());
                dto.setMinimumOrderAmount(r.getMinimumOrderAmount());
                if (r.getGiftItem() != null) {
                    dto.setGiftItemId(r.getGiftItem().getId());
                    dto.setGiftItemName(r.getGiftItem().getDishName());
                }
                return dto;
            } else if (rule instanceof BuyXGetYRule r) {
                BuyXGetYRuleDTO dto = new BuyXGetYRuleDTO();
                dto.setId(r.getId());
                if (r.getRequiredItem() != null) {
                    dto.setRequiredItemId(r.getRequiredItem().getId());
                    dto.setRequiredItemName(r.getRequiredItem().getDishName());
                }
                dto.setRequiredQuantity(r.getRequiredQuantity());
                if (r.getFreeItem() != null) {
                    dto.setFreeItemId(r.getFreeItem().getId());
                    dto.setFreeItemName(r.getFreeItem().getDishName());
                }
                dto.setFreeQuantity(r.getFreeQuantity());
                dto.setMaxRedemptions(r.getMaxRedemptions());
                return dto;
            } else if (rule instanceof CategoryDiscountRule r) {
                CategoryDiscountRuleDTO dto = new CategoryDiscountRuleDTO();
                dto.setId(r.getId());
                if (r.getCategory() != null) {
                    dto.setCategoryId(r.getCategory().getId());
                    dto.setCategoryName(r.getCategory().getName());
                }
                dto.setDiscountPercentage(r.getDiscountPercentage());
                return dto;
            }
            return null;
        }).collect(Collectors.toList());
    }

    default List<PromotionRule> mapDTOsToRules(List<PromotionRuleDTO> dtos) {
        if (dtos == null) return null;
        return dtos.stream().map(dto -> {
            if (dto instanceof PercentageDiscountRuleDTO d) {
                PercentageDiscountRule rule = new PercentageDiscountRule();
                rule.setId(d.getId());
                rule.setMinimumOrderAmount(d.getMinimumOrderAmount());
                rule.setDiscountPercentage(d.getDiscountPercentage());
                rule.setMaximumDiscount(d.getMaximumDiscount());
                return rule;
            } else if (dto instanceof FixedDiscountRuleDTO d) {
                FixedDiscountRule rule = new FixedDiscountRule();
                rule.setId(d.getId());
                rule.setMinimumOrderAmount(d.getMinimumOrderAmount());
                rule.setDiscountAmount(d.getDiscountAmount());
                return rule;
            } else if (dto instanceof GiftRuleDTO d) {
                GiftRule rule = new GiftRule();
                rule.setId(d.getId());
                rule.setMinimumOrderAmount(d.getMinimumOrderAmount());
                return rule;
            } else if (dto instanceof BuyXGetYRuleDTO d) {
                BuyXGetYRule rule = new BuyXGetYRule();
                rule.setId(d.getId());
                rule.setRequiredQuantity(d.getRequiredQuantity());
                rule.setFreeQuantity(d.getFreeQuantity());
                rule.setMaxRedemptions(d.getMaxRedemptions());
                return rule;
            } else if (dto instanceof CategoryDiscountRuleDTO d) {
                CategoryDiscountRule rule = new CategoryDiscountRule();
                rule.setId(d.getId());
                rule.setDiscountPercentage(d.getDiscountPercentage());
                return rule;
            }
            return null;
        }).collect(Collectors.toList());
    }
}
