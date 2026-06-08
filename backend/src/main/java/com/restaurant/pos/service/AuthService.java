package com.restaurant.pos.service;

import com.restaurant.pos.dto.auth.LoginRequest;
import com.restaurant.pos.dto.auth.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
