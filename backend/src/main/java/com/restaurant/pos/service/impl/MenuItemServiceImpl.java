package com.restaurant.pos.service.impl;

import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.menu.CreateMenuItemRequest;
import com.restaurant.pos.dto.menu.MenuItemResponse;
import com.restaurant.pos.dto.menu.UpdateMenuItemRequest;
import com.restaurant.pos.entity.Category;
import com.restaurant.pos.entity.MenuItem;
import com.restaurant.pos.exception.ResourceNotFoundException;
import com.restaurant.pos.repository.CategoryRepository;
import com.restaurant.pos.repository.MenuItemRepository;
import com.restaurant.pos.service.MenuItemService;
import com.restaurant.pos.service.PageResponseFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Stream;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, CategoryRepository categoryRepository) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PagedResponse<MenuItemResponse> findAll(UUID categoryId, Boolean isActive, Pageable pageable) {
        Stream<MenuItem> stream;
        if (categoryId != null) {
            stream = menuItemRepository.findByCategoryId(categoryId).stream();
        } else {
            stream = menuItemRepository.findAll().stream();
        }
        if (isActive != null) {
            stream = stream.filter(item -> item.isAvailable() == isActive);
        }
        return PageResponseFactory.fromList(stream.map(this::toResponse).toList(), pageable.getPageNumber(), pageable.getPageSize());
    }

    @Override
    public MenuItemResponse findById(UUID id) {
        return toResponse(findMenuItem(id));
    }

    @Override
    @Transactional
    public MenuItemResponse create(CreateMenuItemRequest request) {
        Category category = findCategory(request.getCategoryId());
        MenuItem item = new MenuItem();
        item.setId(UUID.randomUUID());
        item.setDishName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setCategory(category);
        item.setAvailable(request.isActive());
        return toResponse(menuItemRepository.save(item));
    }

    @Override
    @Transactional
    public MenuItemResponse update(UUID id, UpdateMenuItemRequest request) {
        MenuItem item = findMenuItem(id);
        Category category = findCategory(request.getCategoryId());
        item.setDishName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setCategory(category);
        item.setAvailable(request.isActive());
        return toResponse(menuItemRepository.save(item));
    }

    @Override
    @Transactional
    public void disable(UUID id) {
        MenuItem item = findMenuItem(id);
        item.setAvailable(false);
        menuItemRepository.save(item);
    }

    private MenuItem findMenuItem(UUID id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
    }

    private Category findCategory(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    private MenuItemResponse toResponse(MenuItem item) {
        Category category = item.getCategory();
        return new MenuItemResponse(
                item.getId(),
                item.getDishName(),
                item.getDescription(),
                item.getPrice(),
                category == null ? null : category.getId(),
                category == null ? null : category.getName(),
                item.isAvailable()
        );
    }
}
