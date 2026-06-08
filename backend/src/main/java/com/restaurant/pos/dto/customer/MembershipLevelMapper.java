package com.restaurant.pos.dto.customer;

import com.restaurant.pos.entity.MembershipLevel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MembershipLevelMapper {

    MembershipLevelResponse toResponse(MembershipLevel level);

    @Mapping(target = "id", ignore = true)
    MembershipLevel toEntity(CreateMembershipLevelRequest request);

    @Mapping(target = "id", ignore = true)
    MembershipLevel toEntity(UpdateMembershipLevelRequest request);
}
