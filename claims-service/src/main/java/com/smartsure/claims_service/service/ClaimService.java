package com.smartsure.claims_service.service;

import com.smartsure.claims_service.dto.ClaimResponse;
import com.smartsure.claims_service.dto.InitiateClaimRequest;
import com.smartsure.claims_service.payload.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ClaimService {

    ApiResponse<ClaimResponse> createDraftClaim(InitiateClaimRequest request);

    ApiResponse<ClaimResponse> submitClaim(Long claimId);

    ApiResponse<ClaimResponse> getClaimStatus(Long claimId);

    ApiResponse<List<ClaimResponse>> getUserClaims();

    ApiResponse<List<ClaimResponse>> getAllClaims(); // ADMIN

    ApiResponse<String> reviewClaim(Long claimId, String action);
}
