package com.smartsure.admin_service.service;

import com.smartsure.admin_service.dto.*;
import com.smartsure.admin_service.feign.AuthClient;
import com.smartsure.admin_service.feign.ClaimsClient;
import com.smartsure.admin_service.feign.PolicyClient;
import com.smartsure.admin_service.payload.ApiResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    @Mock
    private AuthClient authClient;

    @Mock
    private ClaimsClient claimsClient;

    @Mock
    private PolicyClient policyClient;

    @InjectMocks
    private AdminServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ================= GET ALL USERS =================

    @Test
    void getAllUsers_shouldReturnUsers() {

        ApiResponse<List<UserResponse>> response =
                ApiResponse.<List<UserResponse>>builder()
                        .success(true)
                        .data(List.of(new UserResponse()))
                        .build();

        when(authClient.getAllUsers()).thenReturn(response);

        ApiResponse<List<UserResponse>> result = service.getAllUsers();

        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
    }

    // ================= GET ALL POLICIES =================

    @Test
    void getAllPolicies_shouldReturnPolicies() {

        ApiResponse<List<PolicyResponse>> response =
                ApiResponse.<List<PolicyResponse>>builder()
                        .success(true)   // ✅ FIXED (important)
                        .data(List.of(new PolicyResponse()))
                        .build();

        when(policyClient.getAllPolicies()).thenReturn(response);

        ApiResponse<List<PolicyResponse>> result = service.getAllPolicies();

        assertTrue(result.isSuccess());
        assertEquals(1, result.getData().size());
    }

    // ================= GET ALL CLAIMS =================

    @Test
    void getAllClaims_shouldReturnClaims() {

        ApiResponse<List<ClaimResponse>> response =
                ApiResponse.<List<ClaimResponse>>builder()
                        .success(true)   // ✅ FIXED
                        .data(List.of(new ClaimResponse()))
                        .build();

        when(claimsClient.getAllClaims()).thenReturn(response);

        ApiResponse<List<ClaimResponse>> result = service.getAllClaims();

        assertTrue(result.isSuccess());
        assertEquals(1, result.getData().size());
    }

    // ================= POLICY COUNT =================

    @Test
    void getPolicyCount_shouldReturnCount() {

        ApiResponse<List<PolicyResponse>> response =
                ApiResponse.<List<PolicyResponse>>builder()
                        .success(true)   // ✅ FIXED
                        .data(List.of(new PolicyResponse(), new PolicyResponse()))
                        .build();

        when(policyClient.getAllPolicies()).thenReturn(response);

        ApiResponse<Long> result = service.getPolicyCount();

        assertEquals(2L, result.getData());   // ✅ FIXED (Long type)
    }

    // ================= CLAIM COUNT =================

    @Test
    void getClaimCount_shouldReturnCount() {

        ApiResponse<List<ClaimResponse>> response =
                ApiResponse.<List<ClaimResponse>>builder()
                        .success(true)   // ✅ FIXED
                        .data(List.of(new ClaimResponse(), new ClaimResponse()))
                        .build();

        when(claimsClient.getAllClaims()).thenReturn(response);

        ApiResponse<Long> result = service.getClaimCount();

        assertEquals(2L, result.getData());   // ✅ FIXED
    }

    // ================= CLAIM STATUS REPORT =================

    @Test
    void getClaimStatusReport_shouldGroupByStatus() {

        ClaimResponse c1 = new ClaimResponse();
        c1.setStatus("APPROVED");

        ClaimResponse c2 = new ClaimResponse();
        c2.setStatus("APPROVED");

        ClaimResponse c3 = new ClaimResponse();
        c3.setStatus("REJECTED");

        ApiResponse<List<ClaimResponse>> response =
                ApiResponse.<List<ClaimResponse>>builder()
                        .success(true)   // ✅ FIXED
                        .data(List.of(c1, c2, c3))
                        .build();

        when(claimsClient.getAllClaims()).thenReturn(response);

        ApiResponse<Map<String, Long>> result = service.getClaimStatusReport();

        assertEquals(2L, result.getData().get("APPROVED"));  // ✅ FIXED
        assertEquals(1L, result.getData().get("REJECTED"));
    }
}