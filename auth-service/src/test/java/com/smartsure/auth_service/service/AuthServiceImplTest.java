package com.smartsure.auth_service.service;

import com.smartsure.auth_service.dto.*;
import com.smartsure.auth_service.entity.SmartUser;
import com.smartsure.auth_service.enums.Role;
import com.smartsure.auth_service.exception.InvalidCredentialsException;
import com.smartsure.auth_service.exception.UserAlreadyExistsException;
import com.smartsure.auth_service.payload.ApiResponse;
import com.smartsure.auth_service.repository.SmartUserRepository;
import com.smartsure.auth_service.security.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private SmartUserRepository repository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ================= REGISTER =================

    @Test
    void register_shouldCreateUserSuccessfully() {

        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("1234");

        SmartUser user = new SmartUser();
        SmartUser savedUser = new SmartUser();
        RegisterResponse responseDto = new RegisterResponse();

        when(repository.existsByEmail(request.getEmail())).thenReturn(false);
        when(mapper.map(request, SmartUser.class)).thenReturn(user);
        when(repository.save(any())).thenReturn(savedUser);
        when(mapper.map(savedUser, RegisterResponse.class)).thenReturn(responseDto);

        ApiResponse<RegisterResponse> response = authService.register(request);

        assertTrue(response.isSuccess());
        assertEquals("User registered successfully", response.getMessage());
        verify(repository, times(1)).save(any());
    }

    @Test
    void register_shouldThrowException_whenUserExists() {

        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@gmail.com");

        when(repository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> authService.register(request));

        verify(repository, never()).save(any());
    }

    // ================= LOGIN =================

    @Test
    void login_shouldReturnToken_whenValidCredentials() {

        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("1234");

        SmartUser user = new SmartUser();
        user.setEmail("test@gmail.com");
        user.setRole(Role.CUSTOMER);

        when(repository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(jwtUtil.generateToken(anyString(), anyString()))
                .thenReturn("mock-token");

        ApiResponse<AuthResponse> response = authService.login(request);

        assertTrue(response.isSuccess());
        assertEquals("Login successful", response.getMessage());
        assertEquals("mock-token", response.getData().getToken());
    }

    @Test
    void login_shouldThrowException_whenInvalidCredentials() {

        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("wrong");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any());

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(request));
    }

    // ================= GET USER =================

    @Test
    void getUserByEmail_shouldReturnUser() {

        SmartUser user = new SmartUser();
        user.setId(1L);
        user.setEmail("test@gmail.com");
        user.setRole(Role.CUSTOMER);

        when(repository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        UserResponse response = authService.getUserByEmail("test@gmail.com");

        assertEquals("test@gmail.com", response.getEmail());
        assertEquals("CUSTOMER", response.getRole());
    }

    @Test
    void getUserByEmail_shouldThrowException_whenNotFound() {

        when(repository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> authService.getUserByEmail("test@gmail.com"));
    }

    // ================= GET ALL USERS =================

    @Test
    void getAllUsers_shouldReturnList() {

        SmartUser user = new SmartUser();
        user.setEmail("test@gmail.com");

        UserResponse userResponse = new UserResponse();
        userResponse.setEmail("test@gmail.com");

        when(repository.findAll()).thenReturn(List.of(user));
        when(mapper.map(user, UserResponse.class)).thenReturn(userResponse);

        ApiResponse<List<UserResponse>> response = authService.getAllUsers();

        assertTrue(response.isSuccess());
        assertEquals(1, response.getData().size());
    }
}