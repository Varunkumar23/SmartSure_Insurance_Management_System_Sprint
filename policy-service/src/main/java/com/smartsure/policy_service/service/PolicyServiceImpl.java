package com.smartsure.policy_service.service;

import com.smartsure.policy_service.dto.CreatePolicyRequest;
import com.smartsure.policy_service.dto.PolicyResponse;
import com.smartsure.policy_service.dto.UserPolicyResponse;
import com.smartsure.policy_service.dto.UserResponse;
import com.smartsure.policy_service.entity.Policy;
import com.smartsure.policy_service.entity.Status;
import com.smartsure.policy_service.entity.UserPolicy;
import com.smartsure.policy_service.exception.ResourceNotFoundException;
import com.smartsure.policy_service.feigns.AuthClient;
import com.smartsure.policy_service.payload.ApiResponse;
import com.smartsure.policy_service.repository.PolicyRepository;
import com.smartsure.policy_service.repository.UserPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {

    private final ModelMapper modelMapper;
    private final PolicyRepository repository;
    private final UserPolicyRepository userPolicyRepository;
    private final AuthClient authClient;

    private double calculatePremium(int tenure) {
        double base = 1000;
        if (tenure <= 12) return base;
        else if (tenure <= 24) return base * 1.8;
        else return base * 2.5;
    }

    @Override
    public ApiResponse<PolicyResponse> createPolicy(CreatePolicyRequest request) {
        Policy policy = modelMapper.map(request, Policy.class);
        policy.setStatus(Status.CREATED);
        policy.setPremiumAmount(calculatePremium(request.getTenure()));
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


    @Override
    public ApiResponse<String> purchasePolicy(Long policyId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        UserResponse user = authClient.getUserByEmail(email);


        Policy policy = repository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

        policy.setStatus(Status.ACTIVE);
        repository.save(policy);

        UserPolicy userPolicy = UserPolicy.builder()
                .userId(user.getId())
                .userEmail(email)
                .policyId(policyId)
                .purchaseDate(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();

        userPolicyRepository.save(userPolicy);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Policy purchased successfully")
                .data(null)
                .build();
    }

    @Override
    public ApiResponse<List<UserPolicyResponse>> getUserPolicies() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        List<UserPolicy> userPolicies = userPolicyRepository.findByUserEmail(email);

        List<UserPolicyResponse> responseList = userPolicies.stream().map(up -> {

            Policy policy = repository.findById(up.getPolicyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

            return UserPolicyResponse.builder()
                    .policyId(policy.getId())
                    .policyName(policy.getPolicyName())
                    .premiumAmount(policy.getPremiumAmount())
                    .status(up.getStatus())
                    .build();

        }).toList();

        return ApiResponse.<List<UserPolicyResponse>>builder()
                .success(true)
                .message("User policies fetched successfully")
                .data(responseList)
                .build();
    }

    @Override
    public ApiResponse<PolicyResponse> updatePolicy(Long id, CreatePolicyRequest request) {

        Policy policy = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

        policy.setPolicyName(request.getPolicyName());
        policy.setDescription(request.getDescription());
        policy.setTenure(request.getTenure());
        policy.setPremiumAmount(calculatePremium(request.getTenure()));

        Policy updated = repository.save(policy);

        PolicyResponse response = modelMapper.map(updated, PolicyResponse.class);

        return ApiResponse.<PolicyResponse>builder()
                .success(true)
                .message("Policy updated successfully")
                .data(response)
                .build();
    }

    @Override
    public ApiResponse<String> deletePolicy(Long id) {

        Policy policy = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

        repository.delete(policy);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Policy deleted successfully")
                .data(null)
                .build();
    }

    @Override
    public ApiResponse<List<PolicyResponse>> getAllPolicies() {

        List<PolicyResponse> list = repository.findAll()
                .stream()
                .map(policy -> modelMapper.map(policy, PolicyResponse.class))
                .toList();

        return ApiResponse.<List<PolicyResponse>>builder()
                .success(true)
                .message("All policies fetched successfully")
                .data(list)
                .build();
    }

    @Override
    public ApiResponse<String> expirePolicy(Long id) {

        Policy policy = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found"));

        policy.setStatus(Status.EXPIRED);
        repository.save(policy);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Policy expired successfully")
                .data(null)
                .build();
    }

    @Override
    public ApiResponse<String> cancelUserPolicy(Long policyId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        UserPolicy userPolicy = userPolicyRepository.findByUserEmail(email)
                .stream()
                .filter(p -> p.getPolicyId().equals(policyId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found for user"));

        userPolicy.setStatus(Status.CANCELLED);
        userPolicyRepository.save(userPolicy);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Policy cancelled successfully")
                .data(null)
                .build();
    }


}
