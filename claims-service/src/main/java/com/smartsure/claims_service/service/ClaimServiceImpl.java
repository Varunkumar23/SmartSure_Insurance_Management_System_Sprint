package com.smartsure.claims_service.service;

import com.smartsure.claims_service.dto.ClaimResponse;
import com.smartsure.claims_service.dto.InitiateClaimRequest;
import com.smartsure.claims_service.entity.Claim;
import com.smartsure.claims_service.entity.ClaimDocument;
import com.smartsure.claims_service.entity.ClaimStatus;
import com.smartsure.claims_service.entity.DocumentStatus;
import com.smartsure.claims_service.exception.ResourceNotFoundException;
import com.smartsure.claims_service.payload.ApiResponse;
import com.smartsure.claims_service.repository.ClaimDocumentRepository;
import com.smartsure.claims_service.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;
    private final ModelMapper modelMapper;
    private final ClaimDocumentRepository documentRepository;


    @Override
    public ApiResponse<ClaimResponse> createDraftClaim(InitiateClaimRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Claim claim = Claim.builder()
                .policyId(request.getPolicyId())
                .userEmail(email)
                .description(request.getDescription())
                .status(ClaimStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .build();

        Claim saved=claimRepository.save(claim);
        ClaimResponse response = modelMapper.map(saved, ClaimResponse.class);

        return ApiResponse.<ClaimResponse>builder()
                .success(true)
                .message("Draft claim created")
                .data(response)
                .build();
    }

    @Override
    public ApiResponse<ClaimResponse> submitClaim(Long claimId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        if (!claim.getUserEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }

        if (claim.getStatus() != ClaimStatus.DRAFT) {
            throw new RuntimeException("Only draft can be submitted");
        }

        claim.setStatus(ClaimStatus.SUBMITTED);
        Claim saved=claimRepository.save(claim);
        ClaimResponse response = modelMapper.map(saved, ClaimResponse.class);


        return ApiResponse.<ClaimResponse>builder()
                .success(true)
                .message("Claim submitted successfully")
                .data(response)
                .build();

    }

    @Override
    public ApiResponse<ClaimResponse> getClaimStatus(Long claimId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        if (!claim.getUserEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }

        ClaimResponse response = modelMapper.map(claim, ClaimResponse.class);

        return ApiResponse.<ClaimResponse>builder()
                .success(true)
                .message("Claim status fetched")
                .data(response)
                .build();
    }

    @Override
    public ApiResponse<List<ClaimResponse>> getUserClaims() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        List<ClaimResponse> list = claimRepository.findAll()
                .stream()
                .filter(c -> c.getUserEmail().equals(email))
                .map(c -> modelMapper.map(c, ClaimResponse.class))
                .toList();

        return ApiResponse.<List<ClaimResponse>>builder()
                .success(true)
                .message("User claims fetched")
                .data(list)
                .build();
    }

    @Override
    public ApiResponse<List<ClaimResponse>> getAllClaims() {
        List<ClaimResponse> list = claimRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, ClaimResponse.class))
                .toList();

        return ApiResponse.<List<ClaimResponse>>builder()
                .success(true)
                .message("All claims fetched")
                .data(list)
                .build();
    }

    @Override
    public ApiResponse<String> reviewClaim(Long claimId, String action) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        if (claim.getStatus() != ClaimStatus.SUBMITTED) {
            throw new RuntimeException("Invalid claim state");
        }

        List<ClaimDocument> docs = documentRepository.findAll()
                .stream()
                .filter(d -> d.getClaimId().equals(claimId))
                .toList();

        boolean anyRejected = docs.stream()
                .anyMatch(d -> d.getStatus() == DocumentStatus.REJECTED);

        boolean allVerified = docs.stream()
                .allMatch(d -> d.getStatus() == DocumentStatus.VERIFIED);

        if (anyRejected) {
            claim.setStatus(ClaimStatus.REJECTED);
        } else if (allVerified) {
            claim.setStatus(ClaimStatus.APPROVED);
        } else {
            throw new RuntimeException("All documents must be verified before approval");
        }

        claimRepository.save(claim);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Claim reviewed successfully")
                .data(null)
                .build();
    }
}
