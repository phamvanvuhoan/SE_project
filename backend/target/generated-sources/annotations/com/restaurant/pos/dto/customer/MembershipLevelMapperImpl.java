package com.restaurant.pos.dto.customer;

import com.restaurant.pos.entity.MembershipLevel;
import java.math.BigDecimal;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-09T14:43:35+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.11 (Ubuntu)"
)
@Component
public class MembershipLevelMapperImpl implements MembershipLevelMapper {

    @Override
    public MembershipLevelResponse toResponse(MembershipLevel level) {
        if ( level == null ) {
            return null;
        }

        UUID id = null;
        String levelName = null;
        BigDecimal minSpend = null;
        BigDecimal pointRate = null;

        id = level.getId();
        levelName = level.getLevelName();
        minSpend = level.getMinSpend();
        pointRate = level.getPointRate();

        MembershipLevelResponse membershipLevelResponse = new MembershipLevelResponse( id, levelName, minSpend, pointRate );

        return membershipLevelResponse;
    }

    @Override
    public MembershipLevel toEntity(CreateMembershipLevelRequest request) {
        if ( request == null ) {
            return null;
        }

        MembershipLevel membershipLevel = new MembershipLevel();

        membershipLevel.setLevelName( request.getLevelName() );
        membershipLevel.setMinSpend( request.getMinSpend() );
        membershipLevel.setPointRate( request.getPointRate() );

        return membershipLevel;
    }

    @Override
    public MembershipLevel toEntity(UpdateMembershipLevelRequest request) {
        if ( request == null ) {
            return null;
        }

        MembershipLevel membershipLevel = new MembershipLevel();

        membershipLevel.setLevelName( request.getLevelName() );
        membershipLevel.setMinSpend( request.getMinSpend() );
        membershipLevel.setPointRate( request.getPointRate() );

        return membershipLevel;
    }
}
