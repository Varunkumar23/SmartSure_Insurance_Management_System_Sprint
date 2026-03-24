package com.smartsure.policy_service.service;

import com.smartsure.policy_service.dto.CreatePolicyRequest;
import com.smartsure.policy_service.dto.PolicyResponse;
import com.smartsure.policy_service.payload.ApiResponse;

public interface PolicyService {

    ApiResponse<PolicyResponse> createPolicy(CreatePolicyRequest request);

    ApiResponse<PolicyResponse> getPolicy(Long id);
}
