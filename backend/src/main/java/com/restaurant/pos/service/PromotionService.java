package com.restaurant.pos.service;

import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.promotion.CreatePromotionRequest;
import com.restaurant.pos.dto.promotion.PromotionResponse;
import com.restaurant.pos.dto.promotion.PromotionStatusUpdateRequest;
import com.restaurant.pos.dto.promotion.UpdatePromotionRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PromotionService {
    PagedResponse<PromotionResponse> findAll(Pageable pageable);
    PromotionResponse findById(UUID id);
    PromotionResponse create(CreatePromotionRequest request);
    PromotionResponse update(UUID id, UpdatePromotionRequest request);
    PromotionResponse updateStatus(UUID id, PromotionStatusUpdateRequest request);
    void deactivate(UUID id);
}
