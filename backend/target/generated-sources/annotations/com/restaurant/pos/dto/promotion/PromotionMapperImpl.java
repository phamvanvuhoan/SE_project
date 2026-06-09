package com.restaurant.pos.dto.promotion;

import com.restaurant.pos.entity.Employee;
import com.restaurant.pos.entity.EventPromotion;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-09T14:43:35+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.11 (Ubuntu)"
)
@Component
public class PromotionMapperImpl implements PromotionMapper {

    @Override
    public PromotionResponse toResponse(EventPromotion promotion) {
        if ( promotion == null ) {
            return null;
        }

        UUID createdById = null;
        String createdByName = null;
        UUID id = null;
        String name = null;
        String description = null;
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        createdById = promotionCreatedById( promotion );
        createdByName = promotionCreatedByName( promotion );
        id = promotion.getId();
        name = promotion.getName();
        description = promotion.getDescription();
        startDate = promotion.getStartDate();
        endDate = promotion.getEndDate();

        List<PromotionRuleDTO> rules = mapRulesToDTO(promotion.getRules());
        boolean isActive = false;
        boolean isStackable = false;

        PromotionResponse promotionResponse = new PromotionResponse( id, name, description, startDate, endDate, isActive, isStackable, createdById, createdByName, rules );

        return promotionResponse;
    }

    @Override
    public EventPromotion toEntity(CreatePromotionRequest request) {
        if ( request == null ) {
            return null;
        }

        EventPromotion eventPromotion = new EventPromotion();

        eventPromotion.setName( request.getName() );
        eventPromotion.setDescription( request.getDescription() );
        eventPromotion.setStartDate( request.getStartDate() );
        eventPromotion.setEndDate( request.getEndDate() );
        eventPromotion.setActive( request.isActive() );
        eventPromotion.setStackable( request.isStackable() );

        eventPromotion.setRules( mapDTOsToRules(request.getRules()) );

        return eventPromotion;
    }

    @Override
    public EventPromotion toEntity(UpdatePromotionRequest request) {
        if ( request == null ) {
            return null;
        }

        EventPromotion eventPromotion = new EventPromotion();

        eventPromotion.setName( request.getName() );
        eventPromotion.setDescription( request.getDescription() );
        eventPromotion.setStartDate( request.getStartDate() );
        eventPromotion.setEndDate( request.getEndDate() );
        eventPromotion.setActive( request.isActive() );
        eventPromotion.setStackable( request.isStackable() );

        eventPromotion.setRules( mapDTOsToRules(request.getRules()) );

        return eventPromotion;
    }

    private UUID promotionCreatedById(EventPromotion eventPromotion) {
        Employee createdBy = eventPromotion.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        return createdBy.getId();
    }

    private String promotionCreatedByName(EventPromotion eventPromotion) {
        Employee createdBy = eventPromotion.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        return createdBy.getName();
    }
}
