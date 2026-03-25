package com.smartsure.claims_service.controller;

import com.smartsure.claims_service.dto.ClaimResponse;
import com.smartsure.claims_service.payload.ApiResponse;
import com.smartsure.claims_service.service.ClaimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/claims")
@RequiredArgsConstructor
public class AdminClaimController {

    private ClaimService claimService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ClaimResponse>>> getAllClaims() {

        return ResponseEntity.ok(claimService.getAllClaims());
    }

    @PutMapping("/{claimId}/review")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> reviewClaim(
            @PathVariable Long claimId,
            @RequestParam String action) {

        return ResponseEntity.ok(claimService.reviewClaim(claimId, action));
    }
}
