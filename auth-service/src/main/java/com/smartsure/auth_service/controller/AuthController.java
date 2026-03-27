package com.smartsure.auth_service.controller;


import com.smartsure.auth_service.dto.*;
import com.smartsure.auth_service.payload.ApiResponse;
import com.smartsure.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {


        ApiResponse<RegisterResponse> response = authService.register(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        ApiResponse<AuthResponse> response = authService.login(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/user")
    public UserResponse getUserByEmail(@RequestParam String email) {

        return authService.getUserByEmail(email);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        ApiResponse<List<UserResponse>> response = authService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}