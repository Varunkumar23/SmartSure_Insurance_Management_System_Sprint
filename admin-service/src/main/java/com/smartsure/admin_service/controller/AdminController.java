package com.smartsure.admin_service.controller;


import com.smartsure.admin_service.dto.ClaimResponse;
import com.smartsure.admin_service.dto.PolicyResponse;
import com.smartsure.admin_service.dto.UserResponse;
import com.smartsure.admin_service.payload.ApiResponse;
import com.smartsure.admin_service.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/adminserver")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // USERS
    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    // POLICIES
    @GetMapping("/policies")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PolicyResponse>>> getPolicies() {
        return ResponseEntity.ok(adminService.getAllPolicies());
    }

    // CLAIMS
    @GetMapping("/claims")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ClaimResponse>>> getClaims() {
        return ResponseEntity.ok(adminService.getAllClaims());
    }

    // REPORTS

    @GetMapping("/reports/policies/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long>> getPolicyCount() {
        return ResponseEntity.ok(adminService.getPolicyCount());
    }

    @GetMapping("/reports/claims/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Long>> getClaimCount() {
        return ResponseEntity.ok(adminService.getClaimCount());
    }

    @GetMapping("/reports/claims/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getClaimStatusReport() {
        return ResponseEntity.ok(adminService.getClaimStatusReport());
    }
}