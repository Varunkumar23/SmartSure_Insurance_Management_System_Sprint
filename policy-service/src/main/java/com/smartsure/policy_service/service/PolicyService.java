package com.smartsure.policy_service.service;

import com.smartsure.policy_service.dto.CreatePolicyRequest;
import com.smartsure.policy_service.dto.PolicyResponse;
import com.smartsure.policy_service.dto.UserPolicyResponse;
import com.smartsure.policy_service.payload.ApiResponse;

import java.util.List;

public interface PolicyService {

    //ADMIN
    ApiResponse<PolicyResponse> createPolicy(CreatePolicyRequest request);
    ApiResponse<PolicyResponse> updatePolicy(Long id, CreatePolicyRequest request);
    ApiResponse<String> deletePolicy(Long id);
    ApiResponse<String> expirePolicy(Long id);

    //COMMON
    ApiResponse<PolicyResponse> getPolicy(Long id);
    ApiResponse<List<PolicyResponse>> getAllPolicies();

    //CUSTOMER
    ApiResponse<String> purchasePolicy(Long policyId);
    ApiResponse<List<UserPolicyResponse>> getUserPolicies();
    ApiResponse<String> cancelUserPolicy(Long policyId);

}
