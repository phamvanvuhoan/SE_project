package com.restaurant.pos.controller;

import com.restaurant.pos.dto.common.ApiResponse;
import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.table.CreateTableRequest;
import com.restaurant.pos.dto.table.TableResponse;
import com.restaurant.pos.dto.table.TableStatusUpdateRequest;
import com.restaurant.pos.dto.table.UpdateTableRequest;
import com.restaurant.pos.entity.TableStatus;
import com.restaurant.pos.service.TableService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tables")
@Tag(name = "Tables")
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping
    @Operation(summary = "List tables")
    public ApiResponse<PagedResponse<TableResponse>> findAll(@RequestParam(required = false) TableStatus status, Pageable pageable) {
        return ApiResponse.success(tableService.findAll(status, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get table details")
    public ApiResponse<TableResponse> findById(@PathVariable UUID id) {
        return ApiResponse.success(tableService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create table")
    public ResponseEntity<ApiResponse<TableResponse>> create(@Valid @RequestBody CreateTableRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Table created successfully", tableService.create(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update table")
    public ApiResponse<TableResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateTableRequest request) {
        return ApiResponse.success("Table updated successfully", tableService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change table status")
    public ApiResponse<TableResponse> updateStatus(@PathVariable UUID id, @Valid @RequestBody TableStatusUpdateRequest request) {
        return ApiResponse.success("Table status updated successfully", tableService.updateStatus(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete table")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        tableService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
