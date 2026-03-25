package com.smartsure.claims_service.service;

import com.smartsure.claims_service.entity.Claim;
import com.smartsure.claims_service.entity.ClaimDocument;
import com.smartsure.claims_service.entity.DocumentStatus;
import com.smartsure.claims_service.exception.ResourceNotFoundException;
import com.smartsure.claims_service.payload.ApiResponse;
import com.smartsure.claims_service.repository.ClaimDocumentRepository;
import com.smartsure.claims_service.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final ClaimRepository claimRepository;
    private final ClaimDocumentRepository documentRepository;


    @Override
    public ApiResponse<String> uploadDocument(Long claimId, MultipartFile file) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        try {
            String path = "uploads/" + file.getOriginalFilename();
            file.transferTo(new File(path));

            ClaimDocument doc = ClaimDocument.builder()
                    .claimId(claimId)
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .filePath(path)
                    .status(DocumentStatus.PENDING)
                    .build();

            documentRepository.save(doc);

        } catch (Exception e) {
            throw new RuntimeException("Upload failed");
        }

        return ApiResponse.<String>builder()
                .success(true)
                .message("Document uploaded")
                .data(null)
                .build();
    }

    @Override
    public ApiResponse<List<ClaimDocument>> getDocumentsByClaim(Long claimId) {
        List<ClaimDocument> docs = documentRepository.findAll()
                .stream()
                .filter(d -> d.getClaimId().equals(claimId))
                .toList();

        return ApiResponse.<List<ClaimDocument>>builder()
                .success(true)
                .message("Documents fetched")
                .data(docs)
                .build();
    }

    @Override
    public ApiResponse<String> deleteDocument(Long documentId) {
        ClaimDocument doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        documentRepository.delete(doc);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Document deleted")
                .data(null)
                .build();
    }

    @Override
    public ApiResponse<String> verifyDocument(Long documentId) {
        ClaimDocument doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        doc.setStatus(DocumentStatus.VERIFIED);
        documentRepository.save(doc);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Document verified")
                .data(null)
                .build();
    }

    @Override
    public ApiResponse<String> rejectDocument(Long documentId) {
        ClaimDocument doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        doc.setStatus(DocumentStatus.REJECTED);
        documentRepository.save(doc);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Document rejected")
                .data(null)
                .build();
    }
}
