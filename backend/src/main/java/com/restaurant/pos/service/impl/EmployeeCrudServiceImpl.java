package com.restaurant.pos.service.impl;

import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.employee.ChangePasswordRequest;
import com.restaurant.pos.dto.employee.CreateEmployeeRequest;
import com.restaurant.pos.dto.employee.EmployeeResponse;
import com.restaurant.pos.dto.employee.UpdateEmployeeRequest;
import com.restaurant.pos.entity.Employee;
import com.restaurant.pos.exception.BusinessRuleViolationException;
import com.restaurant.pos.exception.ResourceNotFoundException;
import com.restaurant.pos.repository.EmployeeRepository;
import com.restaurant.pos.service.EmployeeCrudService;
import com.restaurant.pos.service.PageResponseFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EmployeeCrudServiceImpl implements EmployeeCrudService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeCrudServiceImpl(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PagedResponse<EmployeeResponse> findAll(Pageable pageable) {
        return PageResponseFactory.fromPage(employeeRepository.findAll(pageable), this::toResponse);
    }

    @Override
    public EmployeeResponse findById(UUID id) {
        return toResponse(findEmployee(id));
    }

    @Override
    @Transactional
    public EmployeeResponse create(CreateEmployeeRequest request) {
        employeeRepository.findByUsername(request.getUsername()).ifPresent(e -> {
            throw new BusinessRuleViolationException("Username already exists.");
        });
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            employeeRepository.findByPhone(request.getPhone()).ifPresent(e -> {
                throw new BusinessRuleViolationException("Phone already exists.");
            });
        }
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setName(request.getName());
        employee.setRole(request.getRole());
        employee.setPhone(blankToNull(request.getPhone()));
        employee.setUsername(request.getUsername());
        employee.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        employee.setActive(request.isActive());
        return toResponse(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public EmployeeResponse update(UUID id, UpdateEmployeeRequest request) {
        Employee employee = findEmployee(id);
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            employeeRepository.findByPhone(request.getPhone())
                    .filter(existing -> !existing.getId().equals(id))
                    .ifPresent(existing -> {
                        throw new BusinessRuleViolationException("Phone already exists.");
                    });
        }
        employee.setName(request.getName());
        employee.setRole(request.getRole());
        employee.setPhone(blankToNull(request.getPhone()));
        employee.setActive(request.isActive());
        return toResponse(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public EmployeeResponse changePassword(UUID id, ChangePasswordRequest request) {
        Employee employee = findEmployee(id);
        if (!passwordEncoder.matches(request.getOldPassword(), employee.getPasswordHash())) {
            throw new BusinessRuleViolationException("Old password is incorrect.");
        }
        employee.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        return toResponse(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public void disable(UUID id) {
        Employee employee = findEmployee(id);
        employee.setActive(false);
        employeeRepository.save(employee);
    }

    private Employee findEmployee(UUID id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    private EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getRole(),
                employee.getPhone(),
                employee.getUsername(),
                employee.isActive()
        );
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
