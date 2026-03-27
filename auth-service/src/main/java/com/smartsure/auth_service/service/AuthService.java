package com.smartsure.auth_service.service;

import com.smartsure.auth_service.dto.*;
import com.smartsure.auth_service.payload.ApiResponse;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface AuthService {

    ApiResponse<RegisterResponse> register(RegisterRequest request);

    ApiResponse<AuthResponse> login(LoginRequest request);

    UserResponse getUserByEmail(String email);

    ApiResponse<List<UserResponse>> getAllUsers();
}
