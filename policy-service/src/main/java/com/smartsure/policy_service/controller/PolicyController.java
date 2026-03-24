package com.smartsure.policy_service.controller;

import com.smartsure.policy_service.dto.CreatePolicyRequest;
import com.smartsure.policy_service.dto.PolicyResponse;
import com.smartsure.policy_service.payload.ApiResponse;
import com.smartsure.policy_service.service.PolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PolicyResponse>> createPolicy(@Valid @RequestBody CreatePolicyRequest request) {

        return ResponseEntity.ok(policyService.createPolicy(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<ApiResponse<PolicyResponse>> getPolicy(@PathVariable Long id) {

        return ResponseEntity.ok(policyService.getPolicy(id));
    }

}
