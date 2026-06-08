package com.restaurant.pos.service;

import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.customer.CreateMembershipLevelRequest;
import com.restaurant.pos.dto.customer.MembershipLevelResponse;
import com.restaurant.pos.dto.customer.UpdateMembershipLevelRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MembershipLevelCrudService {
    PagedResponse<MembershipLevelResponse> findAll(Pageable pageable);
    MembershipLevelResponse findById(UUID id);
    MembershipLevelResponse create(CreateMembershipLevelRequest request);
    MembershipLevelResponse update(UUID id, UpdateMembershipLevelRequest request);
}
