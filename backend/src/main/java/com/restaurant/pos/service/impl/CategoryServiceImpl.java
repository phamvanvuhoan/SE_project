package com.restaurant.pos.service.impl;

import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.menu.CategoryResponse;
import com.restaurant.pos.dto.menu.CreateCategoryRequest;
import com.restaurant.pos.dto.menu.UpdateCategoryRequest;
import com.restaurant.pos.entity.Category;
import com.restaurant.pos.exception.BusinessRuleViolationException;
import com.restaurant.pos.exception.ResourceNotFoundException;
import com.restaurant.pos.repository.CategoryRepository;
import com.restaurant.pos.repository.MenuItemRepository;
import com.restaurant.pos.service.CategoryService;
import com.restaurant.pos.service.PageResponseFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, MenuItemRepository menuItemRepository) {
        this.categoryRepository = categoryRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public PagedResponse<CategoryResponse> findAll(Pageable pageable) {
        return PageResponseFactory.fromPage(categoryRepository.findAll(pageable), this::toResponse);
    }

    @Override
    public CategoryResponse findById(UUID id) {
        return toResponse(findCategory(id));
    }

    @Override
    @Transactional
    public CategoryResponse create(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new BusinessRuleViolationException("Category name already exists.");
        }
        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName(request.getName());
        return toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse update(UUID id, UpdateCategoryRequest request) {
        Category category = findCategory(id);
        categoryRepository.findByName(request.getName())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessRuleViolationException("Category name already exists.");
                });
        category.setName(request.getName());
        return toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Category category = findCategory(id);
        if (!menuItemRepository.findByCategoryId(id).isEmpty()) {
            throw new BusinessRuleViolationException("Cannot delete category because menu items are assigned to it.");
        }
        categoryRepository.delete(category);
    }

    private Category findCategory(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), null);
    }
}
