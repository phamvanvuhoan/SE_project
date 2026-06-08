package com.restaurant.pos.dto.table;

import com.restaurant.pos.entity.RestaurantTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TableMapper {

    @Mapping(target = "seatingCapacity", source = "capacity")
    TableResponse toResponse(RestaurantTable table);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "capacity", source = "seatingCapacity")
    RestaurantTable toEntity(CreateTableRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "capacity", source = "seatingCapacity")
    RestaurantTable toEntity(UpdateTableRequest request);
}

