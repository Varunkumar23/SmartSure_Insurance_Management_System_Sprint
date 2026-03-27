package com.smartsure.policy_service.service;

import com.smartsure.policy_service.dto.*;
import com.smartsure.policy_service.entity.*;
import com.smartsure.policy_service.exception.ResourceNotFoundException;
import com.smartsure.policy_service.feigns.AuthClient;
import com.smartsure.policy_service.payload.ApiResponse;
import com.smartsure.policy_service.producer.PolicyEventProducer;
import com.smartsure.policy_service.repository.PolicyRepository;
import com.smartsure.policy_service.repository.UserPolicyRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PolicyServiceImplTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PolicyRepository repository;

    @Mock
    private UserPolicyRepository userPolicyRepository;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private PolicyServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ================= CREATE POLICY =================

    @Test
    void createPolicy_shouldSuccess() {

        CreatePolicyRequest request = new CreatePolicyRequest();
        request.setPolicyName("Health");
        request.setTenure(12);

        Policy policy = new Policy();
        Policy saved = new Policy();
        PolicyResponse responseDto = new PolicyResponse();

        when(modelMapper.map(request, Policy.class)).thenReturn(policy);
        when(repository.save(any())).thenReturn(saved);
        when(modelMapper.map(saved, PolicyResponse.class)).thenReturn(responseDto);

        ApiResponse<PolicyResponse> response = service.createPolicy(request);

        assertTrue(response.isSuccess());
    }

    // ================= GET POLICY =================

    @Test
    void getPolicy_shouldReturnPolicy() {

        Policy policy = new Policy();

        when(repository.findById(1L)).thenReturn(Optional.of(policy));
        when(modelMapper.map(policy, PolicyResponse.class)).thenReturn(new PolicyResponse());

        ApiResponse<PolicyResponse> response = service.getPolicy(1L);

        assertTrue(response.isSuccess());
    }

    @Test
    void getPolicy_shouldThrowException() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getPolicy(1L));
    }

    // ================= PURCHASE POLICY =================

    @Test
    void purchasePolicy_shouldSuccess() {

        // 🔥 Mock SecurityContext
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@gmail.com");
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 🔥 Mock Feign client
        UserResponse user = new UserResponse();
        user.setId(1L);
        when(authClient.getUserByEmail("test@gmail.com")).thenReturn(user);

        Policy policy = new Policy();
        when(repository.findById(1L)).thenReturn(Optional.of(policy));

        ApiResponse<String> response = service.purchasePolicy(1L);

        assertTrue(response.isSuccess());
        verify(userPolicyRepository, times(1)).save(any());
    }

    // ================= GET USER POLICIES =================

    @Test
    void getUserPolicies_shouldReturnList() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@gmail.com");
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserPolicy userPolicy = UserPolicy.builder()
                .policyId(1L)
                .status(Status.ACTIVE)
                .build();

        Policy policy = new Policy();
        policy.setId(1L);
        policy.setPolicyName("Health");

        when(userPolicyRepository.findByUserEmail("test@gmail.com"))
                .thenReturn(List.of(userPolicy));

        when(repository.findById(1L)).thenReturn(Optional.of(policy));

        ApiResponse<List<UserPolicyResponse>> response = service.getUserPolicies();

        assertTrue(response.isSuccess());
        assertEquals(1, response.getData().size());
    }

    // ================= UPDATE POLICY =================

    @Test
    void updatePolicy_shouldSuccess() {

        Policy policy = new Policy();
        when(repository.findById(1L)).thenReturn(Optional.of(policy));
        when(repository.save(any())).thenReturn(policy);
        when(modelMapper.map(policy, PolicyResponse.class)).thenReturn(new PolicyResponse());

        CreatePolicyRequest request = new CreatePolicyRequest();
        request.setPolicyName("Updated");

        ApiResponse<PolicyResponse> response = service.updatePolicy(1L, request);

        assertTrue(response.isSuccess());
    }

    // ================= DELETE POLICY =================

    @Test
    void deletePolicy_shouldSuccess() {

        Policy policy = new Policy();
        when(repository.findById(1L)).thenReturn(Optional.of(policy));

        ApiResponse<String> response = service.deletePolicy(1L);

        assertTrue(response.isSuccess());
        verify(repository, times(1)).delete(policy);
    }

    // ================= GET ALL =================

    @Test
    void getAllPolicies_shouldReturnList() {

        Policy policy = new Policy();
        when(repository.findAll()).thenReturn(List.of(policy));
        when(modelMapper.map(policy, PolicyResponse.class)).thenReturn(new PolicyResponse());

        ApiResponse<List<PolicyResponse>> response = service.getAllPolicies();

        assertTrue(response.isSuccess());
    }

    // ================= EXPIRE =================

    @Test
    void expirePolicy_shouldSuccess() {

        Policy policy = new Policy();
        when(repository.findById(1L)).thenReturn(Optional.of(policy));

        ApiResponse<String> response = service.expirePolicy(1L);

        assertTrue(response.isSuccess());
    }

    // ================= CANCEL =================

    @Test
    void cancelUserPolicy_shouldSuccess() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@gmail.com");
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserPolicy userPolicy = UserPolicy.builder()
                .policyId(1L)
                .status(Status.ACTIVE)
                .build();

        when(userPolicyRepository.findByUserEmail("test@gmail.com"))
                .thenReturn(List.of(userPolicy));

        ApiResponse<String> response = service.cancelUserPolicy(1L);

        assertTrue(response.isSuccess());
    }
}
