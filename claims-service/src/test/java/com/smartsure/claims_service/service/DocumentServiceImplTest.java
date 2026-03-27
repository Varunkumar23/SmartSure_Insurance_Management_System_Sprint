package com.smartsure.claims_service.service;


import com.smartsure.claims_service.entity.*;
import com.smartsure.claims_service.exception.ResourceNotFoundException;
import com.smartsure.claims_service.payload.ApiResponse;
import com.smartsure.claims_service.repository.ClaimDocumentRepository;
import com.smartsure.claims_service.repository.ClaimRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentServiceImplTest {

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private ClaimDocumentRepository documentRepository;

    @InjectMocks
    private DocumentServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ================= UPLOAD DOCUMENT =================

    @Test
    void uploadDocument_shouldSuccess() throws Exception {

        Claim claim = new Claim();
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "dummy content".getBytes()
        );

        ApiResponse<String> response = service.uploadDocument(1L, file);

        assertTrue(response.isSuccess());
        verify(documentRepository, times(1)).save(any());
    }

    @Test
    void uploadDocument_shouldThrow_whenClaimNotFound() {

        when(claimRepository.findById(1L)).thenReturn(Optional.empty());

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "data".getBytes()
        );

        assertThrows(ResourceNotFoundException.class,
                () -> service.uploadDocument(1L, file));
    }

    @Test
    void uploadDocument_shouldThrow_whenFileEmpty() {

        Claim claim = new Claim();
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                new byte[0] // ❌ empty
        );

        assertThrows(RuntimeException.class,
                () -> service.uploadDocument(1L, file));
    }

    // ================= GET DOCUMENTS =================

    @Test
    void getDocumentsByClaim_shouldReturnList() {

        ClaimDocument doc = ClaimDocument.builder()
                .claimId(1L)
                .build();

        when(documentRepository.findAll()).thenReturn(List.of(doc));

        ApiResponse<List<ClaimDocument>> response =
                service.getDocumentsByClaim(1L);

        assertEquals(1, response.getData().size());
    }

    // ================= DELETE DOCUMENT =================

    @Test
    void deleteDocument_shouldSuccess() {

        ClaimDocument doc = new ClaimDocument();

        when(documentRepository.findById(1L)).thenReturn(Optional.of(doc));

        ApiResponse<String> response = service.deleteDocument(1L);

        assertTrue(response.isSuccess());
        verify(documentRepository).delete(doc);
    }

    @Test
    void deleteDocument_shouldThrow_whenNotFound() {

        when(documentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.deleteDocument(1L));
    }

    // ================= VERIFY DOCUMENT =================

    @Test
    void verifyDocument_shouldSuccess() {

        ClaimDocument doc = new ClaimDocument();

        when(documentRepository.findById(1L)).thenReturn(Optional.of(doc));

        ApiResponse<String> response = service.verifyDocument(1L);

        assertTrue(response.isSuccess());
        assertEquals(DocumentStatus.VERIFIED, doc.getStatus());
    }

    // ================= REJECT DOCUMENT =================

    @Test
    void rejectDocument_shouldSuccess() {

        ClaimDocument doc = new ClaimDocument();

        when(documentRepository.findById(1L)).thenReturn(Optional.of(doc));

        ApiResponse<String> response = service.rejectDocument(1L);

        assertTrue(response.isSuccess());
        assertEquals(DocumentStatus.REJECTED, doc.getStatus());
    }
}
