package com.smartsure.claims_service.service;


import com.smartsure.claims_service.dto.*;
import com.smartsure.claims_service.entity.*;
import com.smartsure.claims_service.exception.ResourceNotFoundException;
import com.smartsure.claims_service.payload.ApiResponse;
import com.smartsure.claims_service.repository.ClaimDocumentRepository;
import com.smartsure.claims_service.repository.ClaimRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClaimServiceImplTest {

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private ClaimDocumentRepository documentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ClaimServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ================= CREATE DRAFT =================

    @Test
    void createDraftClaim_shouldSuccess() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@gmail.com");
        SecurityContextHolder.getContext().setAuthentication(auth);

        InitiateClaimRequest request = new InitiateClaimRequest();
        request.setPolicyId(1L);
        request.setDescription("Test");

        Claim saved = new Claim();
        ClaimResponse responseDto = new ClaimResponse();

        when(claimRepository.save(any())).thenReturn(saved);
        when(modelMapper.map(saved, ClaimResponse.class)).thenReturn(responseDto);

        ApiResponse<ClaimResponse> response = service.createDraftClaim(request);

        assertTrue(response.isSuccess());
    }

    // ================= SUBMIT CLAIM =================

    @Test
    void submitClaim_shouldSuccess() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@gmail.com");
        SecurityContextHolder.getContext().setAuthentication(auth);

        Claim claim = Claim.builder()
                .id(1L)
                .userEmail("test@gmail.com")
                .status(ClaimStatus.DRAFT)
                .build();

        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));
        when(claimRepository.save(any())).thenReturn(claim);
        when(modelMapper.map(any(), eq(ClaimResponse.class))).thenReturn(new ClaimResponse());

        ApiResponse<ClaimResponse> response = service.submitClaim(1L);

        assertTrue(response.isSuccess());
    }

    @Test
    void submitClaim_shouldThrow_whenNotFound() {

        when(claimRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.submitClaim(1L));
    }

    // ================= GET CLAIM STATUS =================

    @Test
    void getClaimStatus_shouldSuccess() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@gmail.com");
        SecurityContextHolder.getContext().setAuthentication(auth);

        Claim claim = Claim.builder()
                .id(1L)
                .userEmail("test@gmail.com")
                .build();

        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));
        when(modelMapper.map(any(), eq(ClaimResponse.class))).thenReturn(new ClaimResponse());

        ApiResponse<ClaimResponse> response = service.getClaimStatus(1L);

        assertTrue(response.isSuccess());
    }

    // ================= GET USER CLAIMS =================

    @Test
    void getUserClaims_shouldReturnList() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@gmail.com");
        SecurityContextHolder.getContext().setAuthentication(auth);

        Claim claim = Claim.builder()
                .userEmail("test@gmail.com")
                .build();

        when(claimRepository.findAll()).thenReturn(List.of(claim));
        when(modelMapper.map(any(), eq(ClaimResponse.class))).thenReturn(new ClaimResponse());

        ApiResponse<List<ClaimResponse>> response = service.getUserClaims();

        assertEquals(1, response.getData().size());
    }

    // ================= GET ALL CLAIMS =================

    @Test
    void getAllClaims_shouldReturnList() {

        Claim claim = new Claim();

        when(claimRepository.findAll()).thenReturn(List.of(claim));
        when(modelMapper.map(any(), eq(ClaimResponse.class))).thenReturn(new ClaimResponse());

        ApiResponse<List<ClaimResponse>> response = service.getAllClaims();

        assertTrue(response.isSuccess());
    }

    // ================= REVIEW CLAIM =================

    @Test
    void reviewClaim_shouldApprove_whenAllVerified() {

        Claim claim = Claim.builder()
                .id(1L)
                .status(ClaimStatus.SUBMITTED)
                .build();

        ClaimDocument doc = ClaimDocument.builder()
                .claimId(1L)
                .status(DocumentStatus.VERIFIED)
                .build();

        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));
        when(documentRepository.findAll()).thenReturn(List.of(doc));

        ApiResponse<String> response = service.reviewClaim(1L, "APPROVE");

        assertTrue(response.isSuccess());
        assertEquals(ClaimStatus.APPROVED, claim.getStatus());
    }

    @Test
    void reviewClaim_shouldReject_whenAnyRejected() {

        Claim claim = Claim.builder()
                .id(1L)
                .status(ClaimStatus.SUBMITTED)
                .build();

        ClaimDocument doc = ClaimDocument.builder()
                .claimId(1L)
                .status(DocumentStatus.REJECTED)
                .build();

        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));
        when(documentRepository.findAll()).thenReturn(List.of(doc));

        ApiResponse<String> response = service.reviewClaim(1L, "REJECT");

        assertTrue(response.isSuccess());
        assertEquals(ClaimStatus.REJECTED, claim.getStatus());
    }

    @Test
    void reviewClaim_shouldThrow_whenInvalidState() {

        Claim claim = Claim.builder()
                .id(1L)
                .status(ClaimStatus.DRAFT)
                .build();

        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));

        assertThrows(RuntimeException.class,
                () -> service.reviewClaim(1L, "APPROVE"));
    }
}
