package com.restaurant.pos.service;

import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.menu.CreateMenuItemRequest;
import com.restaurant.pos.dto.menu.MenuItemResponse;
import com.restaurant.pos.dto.menu.UpdateMenuItemRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MenuItemService {
    PagedResponse<MenuItemResponse> findAll(UUID categoryId, Boolean isActive, Pageable pageable);
    MenuItemResponse findById(UUID id);
    MenuItemResponse create(CreateMenuItemRequest request);
    MenuItemResponse update(UUID id, UpdateMenuItemRequest request);
    void disable(UUID id);
}
