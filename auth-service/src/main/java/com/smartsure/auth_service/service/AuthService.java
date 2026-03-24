package com.smartsure.auth_service.service;

import com.smartsure.auth_service.dto.RegisterRequest;
import com.smartsure.auth_service.dto.RegisterResponse;
import com.smartsure.auth_service.payload.ApiResponse;
import com.smartsure.auth_service.dto.AuthResponse;
import com.smartsure.auth_service.dto.LoginRequest;

public interface AuthService {

    ApiResponse<RegisterResponse> register(RegisterRequest request);

    ApiResponse<AuthResponse> login(LoginRequest request);
}
