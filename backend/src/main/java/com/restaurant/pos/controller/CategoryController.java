package com.restaurant.pos.controller;

import com.restaurant.pos.dto.common.ApiResponse;
import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.menu.CategoryResponse;
import com.restaurant.pos.dto.menu.CreateCategoryRequest;
import com.restaurant.pos.dto.menu.UpdateCategoryRequest;
import com.restaurant.pos.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "List categories")
    public ApiResponse<PagedResponse<CategoryResponse>> findAll(Pageable pageable) {
        return ApiResponse.success(categoryService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category details")
    public ApiResponse<CategoryResponse> findById(@PathVariable UUID id) {
        return ApiResponse.success(categoryService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create category")
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CreateCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Category created successfully", categoryService.create(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category")
    public ApiResponse<CategoryResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateCategoryRequest request) {
        return ApiResponse.success("Category updated successfully", categoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
