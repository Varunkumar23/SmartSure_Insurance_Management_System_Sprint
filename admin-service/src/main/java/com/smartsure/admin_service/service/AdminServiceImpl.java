package com.smartsure.admin_service.service;

import com.smartsure.admin_service.dto.ClaimResponse;
import com.smartsure.admin_service.dto.PolicyResponse;
import com.smartsure.admin_service.dto.UserResponse;
import com.smartsure.admin_service.feign.AuthClient;
import com.smartsure.admin_service.feign.ClaimsClient;
import com.smartsure.admin_service.feign.PolicyClient;
import com.smartsure.admin_service.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AuthClient authClient;
    private final ClaimsClient claimsClient;
    private final PolicyClient policyClient;

    @Override
    @Cacheable("users")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        System.out.println("Fetch all the users from User Service");

        return authClient.getAllUsers();
    }


    @Override
    @Cacheable("adminPolicies")
    public ApiResponse<List<PolicyResponse>> getAllPolicies() {
        System.out.println("Fetch all the policies from Policy Service");



        List<PolicyResponse> policies = policyClient.getAllPolicies().getData();

        return ApiResponse.<List<PolicyResponse>>builder()
                .success(true)
                .message("Policies fetched successfully")
                .data(policies)
                .build();
    }

    @Override
    @Cacheable("adminClaims")
    public ApiResponse<List<ClaimResponse>> getAllClaims() {
        System.out.println("Fetch all the claims from Claim Service");

        List<ClaimResponse> claims = claimsClient.getAllClaims().getData();

        return ApiResponse.<List<ClaimResponse>>builder()
                .success(true)
                .message("Claims fetched successfully")
                .data(claims)
                .build();
    }

    @Override
    @Cacheable("adminPolicyCount")
    public ApiResponse<Long> getPolicyCount() {

        long count = policyClient.getAllPolicies().getData().size();

        return ApiResponse.<Long>builder()
                .success(true)
                .message("Policy count fetched")
                .data(count)
                .build();
    }

    @Override
    public ApiResponse<Long> getClaimCount() {

        long count = claimsClient.getAllClaims().getData().size();

        return ApiResponse.<Long>builder()
                .success(true)
                .message("Claim count fetched")
                .data(count)
                .build();
    }

    @Override
    public ApiResponse<Map<String, Long>> getClaimStatusReport() {

        List<ClaimResponse> claims = claimsClient.getAllClaims().getData();

        Map<String, Long> report = claims.stream()
                .collect(Collectors.groupingBy(
                        ClaimResponse::getStatus,
                        Collectors.counting()
                ));

        return ApiResponse.<Map<String, Long>>builder()
                .success(true)
                .message("Claim status report generated")
                .data(report)
                .build();
    }


}
