package com.restaurant.pos.service.impl;

import com.restaurant.pos.dto.auth.LoginRequest;
import com.restaurant.pos.dto.auth.LoginResponse;
import com.restaurant.pos.entity.Employee;
import com.restaurant.pos.exception.BusinessRuleViolationException;
import com.restaurant.pos.repository.EmployeeRepository;
import com.restaurant.pos.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Employee employee = employeeRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessRuleViolationException("Invalid username or password."));
        if (!employee.isActive() || !passwordEncoder.matches(request.getPassword(), employee.getPasswordHash())) {
            throw new BusinessRuleViolationException("Invalid username or password.");
        }
        String tokenPayload = employee.getId() + ":" + employee.getRole() + ":" + UUID.randomUUID();
        String token = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(tokenPayload.getBytes(StandardCharsets.UTF_8));
        return new LoginResponse(employee.getId(), employee.getName(), employee.getRole().name(), token);
    }
}
