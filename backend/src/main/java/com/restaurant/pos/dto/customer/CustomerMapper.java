package com.restaurant.pos.dto.customer;

import com.restaurant.pos.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "membershipLevelName", source = "membershipLevel.levelName")
    CustomerResponse toResponse(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalPoints", ignore = true)
    @Mapping(target = "totalSpent", ignore = true)
    @Mapping(target = "membershipLevel", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Customer toEntity(CreateCustomerRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalPoints", ignore = true)
    @Mapping(target = "totalSpent", ignore = true)
    @Mapping(target = "membershipLevel", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Customer toEntity(UpdateCustomerRequest request);
}
