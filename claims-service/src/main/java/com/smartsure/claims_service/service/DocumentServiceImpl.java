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

        // 🔹 1. Validate claim
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        // 🔹 2. Validate file
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty or missing");
        }

        try {
            // 🔹 3. Create uploads directory (absolute path)
            String uploadDirPath = System.getProperty("user.dir") + File.separator + "uploads";
            File uploadDir = new File(uploadDirPath);

            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (!created) {
                    throw new RuntimeException("Failed to create upload directory");
                }
            }

            // 🔹 4. Generate unique filename (prevents overwrite)
            String originalFileName = file.getOriginalFilename();
            String safeFileName = originalFileName != null
                    ? originalFileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_")
                    : "file";

            String fileName = System.currentTimeMillis() + "_" + safeFileName;

            // 🔹 5. Full file path
            String filePath = uploadDir.getAbsolutePath() + File.separator + fileName;

            File destination = new File(filePath);

            // 🔹 6. Save file
            file.transferTo(destination);

            // 🔹 7. Save metadata in DB
            ClaimDocument doc = ClaimDocument.builder()
                    .claimId(claimId)
                    .fileName(fileName)
                    .fileType(file.getContentType())
                    .filePath(filePath)
                    .status(DocumentStatus.PENDING)
                    .build();

            documentRepository.save(doc);

            // 🔹 8. Success response
            return ApiResponse.<String>builder()
                    .success(true)
                    .message("Document uploaded successfully")
                    .data(fileName)
                    .build();

        } catch (Exception e) {
            e.printStackTrace(); // 🔥 important for debugging
            throw new RuntimeException("Upload failed: " + e.getMessage());
        }
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
