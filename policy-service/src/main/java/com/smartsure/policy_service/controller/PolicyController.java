package com.smartsure.policy_service.controller;

import com.smartsure.policy_service.dto.CreatePolicyRequest;
import com.smartsure.policy_service.dto.PolicyResponse;
import com.smartsure.policy_service.dto.UserPolicyResponse;
import com.smartsure.policy_service.payload.ApiResponse;
import com.smartsure.policy_service.service.PolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<ApiResponse<PolicyResponse>> getPolicy(@PathVariable Long id) {

        return ResponseEntity.ok(policyService.getPolicy(id));
    }

    @PostMapping("/purchase/{policyId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<String>> purchasePolicy(@PathVariable Long policyId) {

        return ResponseEntity.ok(policyService.purchasePolicy(policyId));
    }

    @GetMapping("/my-policies")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<List<UserPolicyResponse>>> getUserPolicies() {

        return ResponseEntity.ok(policyService.getUserPolicies());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<ApiResponse<List<PolicyResponse>>> getAllPolicies() {

        return ResponseEntity.ok(policyService.getAllPolicies());
    }

    @PutMapping("/cancel/{policyId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<String>> cancelPolicy(@PathVariable Long policyId) {

        return ResponseEntity.ok(policyService.cancelUserPolicy(policyId));
    }


}
