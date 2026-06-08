package com.restaurant.pos.dto.menu;

import com.restaurant.pos.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "description", ignore = true)
    CategoryResponse toResponse(Category category);

    @Mapping(target = "id", ignore = true)
    Category toEntity(CreateCategoryRequest request);

    @Mapping(target = "id", ignore = true)
    Category toEntity(UpdateCategoryRequest request);
}
