package com.smartsure.auth_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsure.auth_service.dto.*;
import com.smartsure.auth_service.payload.ApiResponse;
import com.smartsure.auth_service.security.CustomUserDetailsService;
import com.smartsure.auth_service.security.JwtUtil;
import com.smartsure.auth_service.service.AuthService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @MockBean
    private CustomUserDetailsService customUserDetailsService;


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    // ================= REGISTER =================

    @Test
    void register_shouldReturnSuccess() throws Exception {

        RegisterRequest request = new RegisterRequest();
        request.setName("Varun");
        request.setEmail("test@gmail.com");
        request.setPassword("1234");
        request.setAddress("India");

        RegisterResponse registerResponse = new RegisterResponse();

        ApiResponse<RegisterResponse> apiResponse = ApiResponse.<RegisterResponse>builder()
                .success(true)
                .message("User registered successfully")
                .data(registerResponse)
                .build();

        when(authService.register(any())).thenReturn(apiResponse);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // ================= LOGIN =================

    @Test
    void login_shouldReturnToken() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("1234");

        AuthResponse authResponse = AuthResponse.builder()
                .token("mock-token")
                .role("CUSTOMER")
                .build();

        ApiResponse<AuthResponse> apiResponse = ApiResponse.<AuthResponse>builder()
                .success(true)
                .data(authResponse)
                .build();

        when(authService.login(any())).thenReturn(apiResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.data.token").value("mock-token"))
                .andExpect(jsonPath("$.data.role").value("CUSTOMER"));
    }

    // ================= GET USER =================

    @Test
    void getUserByEmail_shouldReturnUser() throws Exception {

        UserResponse user = new UserResponse(1L, "test@gmail.com", "CUSTOMER");

        when(authService.getUserByEmail("test@gmail.com")).thenReturn(user);

        mockMvc.perform(get("/api/auth/user")
                        .param("email", "test@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));
    }

    // ================= GET ALL USERS =================

    @Test
    void getAllUsers_shouldReturnList() throws Exception {

        UserResponse user = new UserResponse(1L, "test@gmail.com", "CUSTOMER");

        ApiResponse<List<UserResponse>> response = ApiResponse.<List<UserResponse>>builder()
                .success(true)
                .data(List.of(user))
                .build();

        when(authService.getAllUsers()).thenReturn(response);

        mockMvc.perform(get("/api/auth/getAllUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].email").value("test@gmail.com"))
                .andExpect(jsonPath("$.data[0].role").value("CUSTOMER"));
    }
}