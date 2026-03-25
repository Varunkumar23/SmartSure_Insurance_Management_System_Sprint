package com.smartsure.claims_service.controller;

import com.smartsure.claims_service.dto.ClaimResponse;
import com.smartsure.claims_service.dto.InitiateClaimRequest;
import com.smartsure.claims_service.payload.ApiResponse;
import com.smartsure.claims_service.service.ClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
public class ClaimController {

    private final ClaimService claimService;

    @PostMapping("/draft")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<ClaimResponse>> createDraft(
            @RequestBody InitiateClaimRequest request) {

        return ResponseEntity.ok(claimService.createDraftClaim(request));
    }

    @PutMapping("/submit/{claimId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<ClaimResponse>> submitClaim(@PathVariable Long claimId) {

        return ResponseEntity.ok(claimService.submitClaim(claimId));
    }

    @GetMapping("/status/{claimId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<ClaimResponse>> getStatus(@PathVariable Long claimId) {

        return ResponseEntity.ok(claimService.getClaimStatus(claimId));
    }      


    @GetMapping("/my-claims")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<List<ClaimResponse>>> getUserClaims() {

        return ResponseEntity.ok(claimService.getUserClaims());
    }
}
