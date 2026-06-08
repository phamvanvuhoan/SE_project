package com.restaurant.pos.service;

import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.employee.ChangePasswordRequest;
import com.restaurant.pos.dto.employee.CreateEmployeeRequest;
import com.restaurant.pos.dto.employee.EmployeeResponse;
import com.restaurant.pos.dto.employee.UpdateEmployeeRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EmployeeCrudService {
    PagedResponse<EmployeeResponse> findAll(Pageable pageable);
    EmployeeResponse findById(UUID id);
    EmployeeResponse create(CreateEmployeeRequest request);
    EmployeeResponse update(UUID id, UpdateEmployeeRequest request);
    EmployeeResponse changePassword(UUID id, ChangePasswordRequest request);
    void disable(UUID id);
}
