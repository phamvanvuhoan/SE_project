package com.restaurant.pos.controller;

import com.restaurant.pos.dto.common.ApiResponse;
import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.promotion.CreatePromotionRequest;
import com.restaurant.pos.dto.promotion.PromotionResponse;
import com.restaurant.pos.dto.promotion.PromotionStatusUpdateRequest;
import com.restaurant.pos.dto.promotion.UpdatePromotionRequest;
import com.restaurant.pos.service.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/promotions")
@Tag(name = "Promotions")
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping
    @Operation(summary = "List promotions")
    public ApiResponse<PagedResponse<PromotionResponse>> findAll(Pageable pageable) {
        return ApiResponse.success(promotionService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get promotion details")
    public ApiResponse<PromotionResponse> findById(@PathVariable UUID id) {
        return ApiResponse.success(promotionService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create promotion")
    public ResponseEntity<ApiResponse<PromotionResponse>> create(@Valid @RequestBody CreatePromotionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Promotion created successfully", promotionService.create(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update promotion")
    public ApiResponse<PromotionResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdatePromotionRequest request) {
        return ApiResponse.success("Promotion updated successfully", promotionService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Activate or deactivate promotion")
    public ApiResponse<PromotionResponse> updateStatus(@PathVariable UUID id,
                                                       @Valid @RequestBody PromotionStatusUpdateRequest request) {
        return ApiResponse.success("Promotion status updated successfully", promotionService.updateStatus(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate promotion")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        promotionService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
