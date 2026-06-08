package com.restaurant.pos.service.impl;

import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.table.CreateTableRequest;
import com.restaurant.pos.dto.table.TableResponse;
import com.restaurant.pos.dto.table.TableStatusUpdateRequest;
import com.restaurant.pos.dto.table.UpdateTableRequest;
import com.restaurant.pos.entity.RestaurantTable;
import com.restaurant.pos.entity.TableStatus;
import com.restaurant.pos.exception.BusinessRuleViolationException;
import com.restaurant.pos.exception.ResourceNotFoundException;
import com.restaurant.pos.repository.RestaurantTableRepository;
import com.restaurant.pos.service.PageResponseFactory;
import com.restaurant.pos.service.TableService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TableServiceImpl implements TableService {

    private final RestaurantTableRepository tableRepository;

    public TableServiceImpl(RestaurantTableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Override
    public PagedResponse<TableResponse> findAll(TableStatus status, Pageable pageable) {
        if (status == null) {
            return PageResponseFactory.fromPage(tableRepository.findAll(pageable), this::toResponse);
        }
        return PageResponseFactory.fromList(tableRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .toList(), pageable.getPageNumber(), pageable.getPageSize());
    }

    @Override
    public TableResponse findById(UUID id) {
        return toResponse(findTable(id));
    }

    @Override
    @Transactional
    public TableResponse create(CreateTableRequest request) {
        tableRepository.findByTableNumber(request.getTableNumber()).ifPresent(table -> {
            throw new BusinessRuleViolationException("Table number already exists.");
        });
        RestaurantTable table = new RestaurantTable();
        table.setId(UUID.randomUUID());
        table.setTableNumber(request.getTableNumber());
        table.setCapacity(request.getSeatingCapacity());
        table.setStatus(request.getStatus());
        return toResponse(tableRepository.save(table));
    }

    @Override
    @Transactional
    public TableResponse update(UUID id, UpdateTableRequest request) {
        RestaurantTable table = findTable(id);
        tableRepository.findByTableNumber(request.getTableNumber())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessRuleViolationException("Table number already exists.");
                });
        table.setTableNumber(request.getTableNumber());
        table.setCapacity(request.getSeatingCapacity());
        table.setStatus(request.getStatus());
        return toResponse(tableRepository.save(table));
    }

    @Override
    @Transactional
    public TableResponse updateStatus(UUID id, TableStatusUpdateRequest request) {
        RestaurantTable table = findTable(id);
        table.setStatus(request.getStatus());
        return toResponse(tableRepository.save(table));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        RestaurantTable table = findTable(id);
        tableRepository.delete(table);
    }

    private RestaurantTable findTable(UUID id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + id));
    }

    private TableResponse toResponse(RestaurantTable table) {
        return new TableResponse(table.getId(), table.getTableNumber(), table.getCapacity(), table.getStatus());
    }
}
