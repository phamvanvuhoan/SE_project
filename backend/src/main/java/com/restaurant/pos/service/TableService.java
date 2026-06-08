package com.restaurant.pos.service;

import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.table.CreateTableRequest;
import com.restaurant.pos.dto.table.TableResponse;
import com.restaurant.pos.dto.table.TableStatusUpdateRequest;
import com.restaurant.pos.dto.table.UpdateTableRequest;
import com.restaurant.pos.entity.TableStatus;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TableService {
    PagedResponse<TableResponse> findAll(TableStatus status, Pageable pageable);
    TableResponse findById(UUID id);
    TableResponse create(CreateTableRequest request);
    TableResponse update(UUID id, UpdateTableRequest request);
    TableResponse updateStatus(UUID id, TableStatusUpdateRequest request);
    void delete(UUID id);
}
