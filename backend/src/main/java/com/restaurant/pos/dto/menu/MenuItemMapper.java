package com.restaurant.pos.dto.menu;

import com.restaurant.pos.entity.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {

    @Mapping(target = "name", source = "dishName")
    @Mapping(target = "isActive", source = "available")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    MenuItemResponse toResponse(MenuItem item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "dishName", source = "name")
    @Mapping(target = "available", source = "active")
    MenuItem toEntity(CreateMenuItemRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "dishName", source = "name")
    @Mapping(target = "available", source = "active")
    MenuItem toEntity(UpdateMenuItemRequest request);
}
