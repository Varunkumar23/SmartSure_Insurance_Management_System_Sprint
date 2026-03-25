package com.smartsure.claims_service.service;

import com.smartsure.claims_service.entity.ClaimDocument;
import com.smartsure.claims_service.payload.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    ApiResponse<String> uploadDocument(Long claimId, MultipartFile file);

    ApiResponse<List<ClaimDocument>> getDocumentsByClaim(Long claimId);

    ApiResponse<String> deleteDocument(Long documentId);

    ApiResponse<String> verifyDocument(Long documentId);

    ApiResponse<String> rejectDocument(Long documentId);
}
