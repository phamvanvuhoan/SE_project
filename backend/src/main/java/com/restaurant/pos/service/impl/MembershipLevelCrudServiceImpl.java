package com.restaurant.pos.service.impl;

import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.customer.CreateMembershipLevelRequest;
import com.restaurant.pos.dto.customer.MembershipLevelResponse;
import com.restaurant.pos.dto.customer.UpdateMembershipLevelRequest;
import com.restaurant.pos.entity.MembershipLevel;
import com.restaurant.pos.exception.BusinessRuleViolationException;
import com.restaurant.pos.exception.ResourceNotFoundException;
import com.restaurant.pos.repository.MembershipLevelRepository;
import com.restaurant.pos.service.MembershipLevelCrudService;
import com.restaurant.pos.service.PageResponseFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class MembershipLevelCrudServiceImpl implements MembershipLevelCrudService {

    private final MembershipLevelRepository membershipLevelRepository;

    public MembershipLevelCrudServiceImpl(MembershipLevelRepository membershipLevelRepository) {
        this.membershipLevelRepository = membershipLevelRepository;
    }

    @Override
    public PagedResponse<MembershipLevelResponse> findAll(Pageable pageable) {
        return PageResponseFactory.fromPage(membershipLevelRepository.findAll(pageable), this::toResponse);
    }

    @Override
    public MembershipLevelResponse findById(UUID id) {
        return toResponse(findLevel(id));
    }

    @Override
    @Transactional
    public MembershipLevelResponse create(CreateMembershipLevelRequest request) {
        membershipLevelRepository.findByLevelName(request.getLevelName()).ifPresent(level -> {
            throw new BusinessRuleViolationException("Membership level name already exists.");
        });
        MembershipLevel level = new MembershipLevel();
        level.setId(UUID.randomUUID());
        level.setLevelName(request.getLevelName());
        level.setMinSpend(request.getMinSpend());
        level.setPointRate(request.getPointRate());
        return toResponse(membershipLevelRepository.save(level));
    }

    @Override
    @Transactional
    public MembershipLevelResponse update(UUID id, UpdateMembershipLevelRequest request) {
        MembershipLevel level = findLevel(id);
        membershipLevelRepository.findByLevelName(request.getLevelName())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessRuleViolationException("Membership level name already exists.");
                });
        level.setLevelName(request.getLevelName());
        level.setMinSpend(request.getMinSpend());
        level.setPointRate(request.getPointRate());
        return toResponse(membershipLevelRepository.save(level));
    }

    private MembershipLevel findLevel(UUID id) {
        return membershipLevelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membership level not found with id: " + id));
    }

    private MembershipLevelResponse toResponse(MembershipLevel level) {
        return new MembershipLevelResponse(level.getId(), level.getLevelName(), level.getMinSpend(), level.getPointRate());
    }
}
