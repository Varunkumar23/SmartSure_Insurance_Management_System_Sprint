package com.smartsure.claims_service.controller;

import com.smartsure.claims_service.entity.ClaimDocument;
import com.smartsure.claims_service.payload.ApiResponse;
import com.smartsure.claims_service.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<String>> uploadDocument(
            @RequestParam Long claimId,
            @RequestParam MultipartFile file) {

        return ResponseEntity.ok(documentService.uploadDocument(claimId, file));
    }

    @GetMapping("/{claimId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public ResponseEntity<ApiResponse<List<ClaimDocument>>> getDocuments(
            @PathVariable Long claimId) {

        return ResponseEntity.ok(documentService.getDocumentsByClaim(claimId));
    }

    @DeleteMapping("/{documentId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<String>> deleteDocument(
            @PathVariable Long documentId) {

        return ResponseEntity.ok(documentService.deleteDocument(documentId));
    }

    @PutMapping("/{documentId}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> verifyDocument(
            @PathVariable Long documentId) {

        return ResponseEntity.ok(documentService.verifyDocument(documentId));
    }

    @PutMapping("/{documentId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> rejectDocument(
            @PathVariable Long documentId) {

        return ResponseEntity.ok(documentService.rejectDocument(documentId));
    }
}
