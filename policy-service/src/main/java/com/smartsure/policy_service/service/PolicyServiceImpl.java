package com.smartsure.policy_service.service;

import com.smartsure.policy_service.dto.CreatePolicyRequest;
import com.smartsure.policy_service.dto.PolicyResponse;
import com.smartsure.policy_service.entity.Policy;
import com.smartsure.policy_service.exception.ResourceNotFoundException;
import com.smartsure.policy_service.payload.ApiResponse;
import com.smartsure.policy_service.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {

    private final ModelMapper modelMapper;
    private final PolicyRepository repository;

    @Override
    public ApiResponse<PolicyResponse> createPolicy(CreatePolicyRequest request) {
        Policy policy = modelMapper.map(request, Policy.class);
        Policy saved = repository.save(policy);
        PolicyResponse response = modelMapper.map(saved, PolicyResponse.class);
        return ApiResponse.<PolicyResponse>builder()
                .success(true)
                .message("Policy created successfully")
                .data(response)
                .build();
    }

    @Override
    public ApiResponse<PolicyResponse> getPolicy(Long id) {
        Policy policy = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));
        PolicyResponse response = modelMapper.map(policy, PolicyResponse.class);
        return ApiResponse.<PolicyResponse>builder()
                .success(true)
                .message("Policy fetched successfully")
                .data(response)
                .build();
    }
}
