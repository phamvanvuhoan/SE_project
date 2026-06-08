package com.restaurant.pos.controller;

import com.restaurant.pos.dto.common.ApiResponse;
import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.menu.CreateMenuItemRequest;
import com.restaurant.pos.dto.menu.MenuItemResponse;
import com.restaurant.pos.dto.menu.UpdateMenuItemRequest;
import com.restaurant.pos.service.MenuItemService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/menu-items")
@Tag(name = "Menu Items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping
    @Operation(summary = "List menu items")
    public ApiResponse<PagedResponse<MenuItemResponse>> findAll(@RequestParam(required = false) UUID categoryId,
                                                                @RequestParam(required = false) Boolean isActive,
                                                                Pageable pageable) {
        return ApiResponse.success(menuItemService.findAll(categoryId, isActive, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get menu item details")
    public ApiResponse<MenuItemResponse> findById(@PathVariable UUID id) {
        return ApiResponse.success(menuItemService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create menu item")
    public ResponseEntity<ApiResponse<MenuItemResponse>> create(@Valid @RequestBody CreateMenuItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Menu item created successfully", menuItemService.create(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update menu item")
    public ApiResponse<MenuItemResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateMenuItemRequest request) {
        return ApiResponse.success("Menu item updated successfully", menuItemService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Disable menu item")
    public ResponseEntity<Void> disable(@PathVariable UUID id) {
        menuItemService.disable(id);
        return ResponseEntity.noContent().build();
    }
}
