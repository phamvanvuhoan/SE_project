package com.restaurant.pos.service.impl;

import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.promotion.BuyXGetYRuleDTO;
import com.restaurant.pos.dto.promotion.CategoryDiscountRuleDTO;
import com.restaurant.pos.dto.promotion.CreatePromotionRequest;
import com.restaurant.pos.dto.promotion.FixedDiscountRuleDTO;
import com.restaurant.pos.dto.promotion.GiftRuleDTO;
import com.restaurant.pos.dto.promotion.PercentageDiscountRuleDTO;
import com.restaurant.pos.dto.promotion.PromotionResponse;
import com.restaurant.pos.dto.promotion.PromotionRuleDTO;
import com.restaurant.pos.dto.promotion.PromotionStatusUpdateRequest;
import com.restaurant.pos.dto.promotion.UpdatePromotionRequest;
import com.restaurant.pos.entity.BuyXGetYRule;
import com.restaurant.pos.entity.Category;
import com.restaurant.pos.entity.CategoryDiscountRule;
import com.restaurant.pos.entity.Employee;
import com.restaurant.pos.entity.EventPromotion;
import com.restaurant.pos.entity.FixedDiscountRule;
import com.restaurant.pos.entity.GiftRule;
import com.restaurant.pos.entity.MenuItem;
import com.restaurant.pos.entity.PercentageDiscountRule;
import com.restaurant.pos.entity.PromotionRule;
import com.restaurant.pos.exception.BusinessRuleViolationException;
import com.restaurant.pos.exception.ResourceNotFoundException;
import com.restaurant.pos.repository.CategoryRepository;
import com.restaurant.pos.repository.EmployeeRepository;
import com.restaurant.pos.repository.EventPromotionRepository;
import com.restaurant.pos.repository.MenuItemRepository;
import com.restaurant.pos.service.PageResponseFactory;
import com.restaurant.pos.service.PromotionService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PromotionServiceImpl implements PromotionService {

    private final EventPromotionRepository promotionRepository;
    private final EmployeeRepository employeeRepository;
    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    public PromotionServiceImpl(EventPromotionRepository promotionRepository,
                                EmployeeRepository employeeRepository,
                                MenuItemRepository menuItemRepository,
                                CategoryRepository categoryRepository) {
        this.promotionRepository = promotionRepository;
        this.employeeRepository = employeeRepository;
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PagedResponse<PromotionResponse> findAll(Pageable pageable) {
        return PageResponseFactory.fromPage(promotionRepository.findAll(pageable), this::toResponse);
    }

    @Override
    public PromotionResponse findById(UUID id) {
        return toResponse(findPromotion(id));
    }

    @Override
    @Transactional
    public PromotionResponse create(CreatePromotionRequest request) {
        validateDates(request.getStartDate(), request.getEndDate());
        Employee creator = employeeRepository.findById(request.getCreatedById())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getCreatedById()));
        EventPromotion promotion = new EventPromotion();
        promotion.setId(UUID.randomUUID());
        promotion.setName(request.getName());
        promotion.setDescription(request.getDescription());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setActive(request.isActive());
        promotion.setStackable(request.isStackable());
        promotion.setCreatedBy(creator);
        promotion.setCreatedAt(LocalDateTime.now());
        promotion.setEventType("PROMOTION");
        promotion.setPromotionType(resolvePromotionType(request.getRules()));
        promotion.setRules(toRules(request.getRules(), promotion));
        return toResponse(promotionRepository.save(promotion));
    }

    @Override
    @Transactional
    public PromotionResponse update(UUID id, UpdatePromotionRequest request) {
        validateDates(request.getStartDate(), request.getEndDate());
        EventPromotion promotion = findPromotion(id);
        promotion.setName(request.getName());
        promotion.setDescription(request.getDescription());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setActive(request.isActive());
        promotion.setStackable(request.isStackable());
        promotion.setPromotionType(resolvePromotionType(request.getRules()));
        promotion.getRules().clear();
        promotion.getRules().addAll(toRules(request.getRules(), promotion));
        return toResponse(promotionRepository.save(promotion));
    }

    @Override
    @Transactional
    public PromotionResponse updateStatus(UUID id, PromotionStatusUpdateRequest request) {
        EventPromotion promotion = findPromotion(id);
        promotion.setActive(request.getActive());
        return toResponse(promotionRepository.save(promotion));
    }

    @Override
    @Transactional
    public void deactivate(UUID id) {
        EventPromotion promotion = findPromotion(id);
        promotion.setActive(false);
        promotionRepository.save(promotion);
    }

    private EventPromotion findPromotion(UUID id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found with id: " + id));
    }

    private void validateDates(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new BusinessRuleViolationException("Promotion start date must be before or equal to end date.");
        }
    }

    private String resolvePromotionType(List<PromotionRuleDTO> rules) {
        if (rules == null || rules.isEmpty()) {
            throw new BusinessRuleViolationException("Promotion must contain at least one rule.");
        }
        PromotionRuleDTO first = rules.get(0);
        if (first instanceof PercentageDiscountRuleDTO) return "PERCENTAGE";
        if (first instanceof FixedDiscountRuleDTO) return "FIXED";
        if (first instanceof BuyXGetYRuleDTO) return "BUY_X_GET_Y";
        if (first instanceof GiftRuleDTO) return "GIFT";
        if (first instanceof CategoryDiscountRuleDTO) return "CATEGORY";
        throw new BusinessRuleViolationException("Unsupported promotion rule type.");
    }

    private List<PromotionRule> toRules(List<PromotionRuleDTO> dtos, EventPromotion promotion) {
        return dtos.stream().map(dto -> toRule(dto, promotion)).toList();
    }

    private PromotionRule toRule(PromotionRuleDTO dto, EventPromotion promotion) {
        PromotionRule rule;
        if (dto instanceof PercentageDiscountRuleDTO d) {
            PercentageDiscountRule r = new PercentageDiscountRule();
            r.setMinimumOrderAmount(d.getMinimumOrderAmount());
            r.setDiscountPercentage(d.getDiscountPercentage());
            r.setMaximumDiscount(d.getMaximumDiscount());
            rule = r;
        } else if (dto instanceof FixedDiscountRuleDTO d) {
            FixedDiscountRule r = new FixedDiscountRule();
            r.setMinimumOrderAmount(d.getMinimumOrderAmount());
            r.setDiscountAmount(d.getDiscountAmount());
            rule = r;
        } else if (dto instanceof GiftRuleDTO d) {
            GiftRule r = new GiftRule();
            r.setMinimumOrderAmount(d.getMinimumOrderAmount());
            r.setGiftItem(findMenuItem(d.getGiftItemId()));
            rule = r;
        } else if (dto instanceof BuyXGetYRuleDTO d) {
            BuyXGetYRule r = new BuyXGetYRule();
            r.setRequiredItem(findMenuItem(d.getRequiredItemId()));
            r.setRequiredQuantity(d.getRequiredQuantity());
            r.setFreeItem(findMenuItem(d.getFreeItemId()));
            r.setFreeQuantity(d.getFreeQuantity());
            r.setMaxRedemptions(d.getMaxRedemptions());
            rule = r;
        } else if (dto instanceof CategoryDiscountRuleDTO d) {
            CategoryDiscountRule r = new CategoryDiscountRule();
            r.setCategory(findCategory(d.getCategoryId()));
            r.setDiscountPercentage(d.getDiscountPercentage());
            rule = r;
        } else {
            throw new BusinessRuleViolationException("Unsupported promotion rule type.");
        }
        rule.setId(dto.getId() == null ? UUID.randomUUID() : dto.getId());
        rule.setEventPromotion(promotion);
        return rule;
    }

    private MenuItem findMenuItem(UUID id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
    }

    private Category findCategory(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    private PromotionResponse toResponse(EventPromotion promotion) {
        return new PromotionResponse(
                promotion.getId(),
                promotion.getName(),
                promotion.getDescription(),
                promotion.getStartDate(),
                promotion.getEndDate(),
                promotion.isActive(),
                promotion.isStackable(),
                promotion.getCreatedBy() == null ? null : promotion.getCreatedBy().getId(),
                promotion.getCreatedBy() == null ? null : promotion.getCreatedBy().getName(),
                promotion.getRules().stream().map(this::toRuleDto).toList()
        );
    }

    private PromotionRuleDTO toRuleDto(PromotionRule rule) {
        if (rule instanceof PercentageDiscountRule r) {
            PercentageDiscountRuleDTO dto = new PercentageDiscountRuleDTO();
            dto.setId(r.getId());
            dto.setMinimumOrderAmount(r.getMinimumOrderAmount());
            dto.setDiscountPercentage(r.getDiscountPercentage());
            dto.setMaximumDiscount(r.getMaximumDiscount());
            return dto;
        }
        if (rule instanceof FixedDiscountRule r) {
            FixedDiscountRuleDTO dto = new FixedDiscountRuleDTO();
            dto.setId(r.getId());
            dto.setMinimumOrderAmount(r.getMinimumOrderAmount());
            dto.setDiscountAmount(r.getDiscountAmount());
            return dto;
        }
        if (rule instanceof GiftRule r) {
            GiftRuleDTO dto = new GiftRuleDTO();
            dto.setId(r.getId());
            dto.setMinimumOrderAmount(r.getMinimumOrderAmount());
            if (r.getGiftItem() != null) {
                dto.setGiftItemId(r.getGiftItem().getId());
                dto.setGiftItemName(r.getGiftItem().getDishName());
            }
            return dto;
        }
        if (rule instanceof BuyXGetYRule r) {
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
        }
        if (rule instanceof CategoryDiscountRule r) {
            CategoryDiscountRuleDTO dto = new CategoryDiscountRuleDTO();
            dto.setId(r.getId());
            if (r.getCategory() != null) {
                dto.setCategoryId(r.getCategory().getId());
                dto.setCategoryName(r.getCategory().getName());
            }
            dto.setDiscountPercentage(r.getDiscountPercentage());
            return dto;
        }
        throw new BusinessRuleViolationException("Unsupported promotion rule type.");
    }
}
