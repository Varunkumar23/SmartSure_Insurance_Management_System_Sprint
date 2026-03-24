package com.smartsure.auth_service.service;

import com.smartsure.auth_service.dto.RegisterRequest;
import com.smartsure.auth_service.dto.RegisterResponse;
import com.smartsure.auth_service.entity.SmartUser;
import com.smartsure.auth_service.enums.Role;
import com.smartsure.auth_service.exception.InvalidCredentialsException;
import com.smartsure.auth_service.exception.UserAlreadyExistsException;
import com.smartsure.auth_service.payload.ApiResponse;
import com.smartsure.auth_service.dto.AuthResponse;
import com.smartsure.auth_service.dto.LoginRequest;
import com.smartsure.auth_service.repository.SmartUserRepository;
import com.smartsure.auth_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final SmartUserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ModelMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder(10);

    @Override
    public ApiResponse<RegisterResponse> register(RegisterRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with this email");
        }

        SmartUser user = mapper.map(request, SmartUser.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);
        SmartUser savedUser = repository.save(user);
        RegisterResponse response = mapper.map(savedUser, RegisterResponse.class);
        return ApiResponse.<RegisterResponse>builder()
                .success(true)
                .message("User registered successfully")
                .data(response)
                .build();


    }

    @Override
    public ApiResponse<AuthResponse> login(LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        SmartUser user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResolutionException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        AuthResponse response = AuthResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .build();

        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Login successful")
                .data(response)
                .build();
    }
}
