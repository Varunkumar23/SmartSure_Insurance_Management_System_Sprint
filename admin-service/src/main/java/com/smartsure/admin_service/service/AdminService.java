package com.smartsure.admin_service.service;

import com.smartsure.admin_service.dto.ClaimResponse;
import com.smartsure.admin_service.dto.PolicyResponse;
import com.smartsure.admin_service.dto.UserResponse;
import com.smartsure.admin_service.payload.ApiResponse;

import java.util.List;
import java.util.Map;

public interface AdminService {

    ApiResponse<List<UserResponse>> getAllUsers();

    ApiResponse<List<PolicyResponse>> getAllPolicies();

    ApiResponse<List<ClaimResponse>> getAllClaims();

    ApiResponse<Long> getPolicyCount();

    ApiResponse<Long> getClaimCount();

    ApiResponse<Map<String, Long>> getClaimStatusReport();
}
