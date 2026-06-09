package com.restaurant.pos.service.impl;

import com.restaurant.pos.dto.auth.LoginRequest;
import com.restaurant.pos.dto.auth.LoginResponse;
import com.restaurant.pos.entity.Employee;
import com.restaurant.pos.exception.BusinessRuleViolationException;
import com.restaurant.pos.repository.EmployeeRepository;
import com.restaurant.pos.security.JwtService;
import com.restaurant.pos.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(EmployeeRepository employeeRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Employee employee = employeeRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessRuleViolationException("Invalid username or password."));
        if (!employee.isActive() || !passwordEncoder.matches(request.getPassword(), employee.getPasswordHash())) {
            throw new BusinessRuleViolationException("Invalid username or password.");
        }
        String token = jwtService.generateToken(employee.getId(), employee.getUsername(), employee.getRole().name());
        return new LoginResponse(employee.getId(), employee.getName(), employee.getRole().name(), token);
    }
}
