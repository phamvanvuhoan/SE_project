package com.restaurant.pos.controller;

import com.restaurant.pos.dto.common.ApiResponse;
import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.employee.ChangePasswordRequest;
import com.restaurant.pos.dto.employee.CreateEmployeeRequest;
import com.restaurant.pos.dto.employee.EmployeeResponse;
import com.restaurant.pos.dto.employee.UpdateEmployeeRequest;
import com.restaurant.pos.service.EmployeeCrudService;
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
@RequestMapping("/api/v1/employees")
@Tag(name = "Employees")
public class EmployeeController {

    private final EmployeeCrudService employeeService;

    public EmployeeController(EmployeeCrudService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @Operation(summary = "List employees")
    public ApiResponse<PagedResponse<EmployeeResponse>> findAll(Pageable pageable) {
        return ApiResponse.success(employeeService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee details")
    public ApiResponse<EmployeeResponse> findById(@PathVariable UUID id) {
        return ApiResponse.success(employeeService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create employee")
    public ResponseEntity<ApiResponse<EmployeeResponse>> create(@Valid @RequestBody CreateEmployeeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Employee created successfully", employeeService.create(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employee")
    public ApiResponse<EmployeeResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateEmployeeRequest request) {
        return ApiResponse.success("Employee updated successfully", employeeService.update(id, request));
    }

    @PatchMapping("/{id}/change-password")
    @Operation(summary = "Change employee password")
    public ApiResponse<EmployeeResponse> changePassword(@PathVariable UUID id, @Valid @RequestBody ChangePasswordRequest request) {
        return ApiResponse.success("Password changed successfully", employeeService.changePassword(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Disable employee")
    public ResponseEntity<Void> disable(@PathVariable UUID id) {
        employeeService.disable(id);
        return ResponseEntity.noContent().build();
    }
}
