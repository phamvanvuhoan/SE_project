package com.restaurant.pos.service;

import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.menu.CategoryResponse;
import com.restaurant.pos.dto.menu.CreateCategoryRequest;
import com.restaurant.pos.dto.menu.UpdateCategoryRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CategoryService {
    PagedResponse<CategoryResponse> findAll(Pageable pageable);
    CategoryResponse findById(UUID id);
    CategoryResponse create(CreateCategoryRequest request);
    CategoryResponse update(UUID id, UpdateCategoryRequest request);
    void delete(UUID id);
}
