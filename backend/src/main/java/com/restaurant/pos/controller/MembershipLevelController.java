package com.restaurant.pos.controller;

import com.restaurant.pos.dto.common.ApiResponse;
import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.customer.CreateMembershipLevelRequest;
import com.restaurant.pos.dto.customer.MembershipLevelResponse;
import com.restaurant.pos.dto.customer.UpdateMembershipLevelRequest;
import com.restaurant.pos.service.MembershipLevelCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/membership-levels")
@Tag(name = "Membership Levels")
public class MembershipLevelController {

    private final MembershipLevelCrudService membershipLevelService;

    public MembershipLevelController(MembershipLevelCrudService membershipLevelService) {
        this.membershipLevelService = membershipLevelService;
    }

    @GetMapping
    @Operation(summary = "List membership levels")
    public ApiResponse<PagedResponse<MembershipLevelResponse>> findAll(Pageable pageable) {
        return ApiResponse.success(membershipLevelService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get membership level details")
    public ApiResponse<MembershipLevelResponse> findById(@PathVariable UUID id) {
        return ApiResponse.success(membershipLevelService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create membership level")
    public ResponseEntity<ApiResponse<MembershipLevelResponse>> create(@Valid @RequestBody CreateMembershipLevelRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Membership level created successfully", membershipLevelService.create(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update membership level")
    public ApiResponse<MembershipLevelResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateMembershipLevelRequest request) {
        return ApiResponse.success("Membership level updated successfully", membershipLevelService.update(id, request));
    }
}
