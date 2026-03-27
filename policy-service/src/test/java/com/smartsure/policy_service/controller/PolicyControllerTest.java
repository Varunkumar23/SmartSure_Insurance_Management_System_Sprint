package com.smartsure.policy_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsure.policy_service.dto.PolicyResponse;
import com.smartsure.policy_service.dto.UserPolicyResponse;
import com.smartsure.policy_service.payload.ApiResponse;
import com.smartsure.policy_service.security.JwtAuthenticationFilter;
import com.smartsure.policy_service.security.JwtUtil;
import com.smartsure.policy_service.service.PolicyService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PolicyController.class)
@AutoConfigureMockMvc(addFilters = false)
class PolicyControllerTest {

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PolicyService policyService;

    @Autowired
    private ObjectMapper objectMapper;

    // ================= GET POLICY =================

    @Test
    void getPolicy_shouldReturnPolicy() throws Exception {

        PolicyResponse policy = new PolicyResponse();

        ApiResponse<PolicyResponse> response = ApiResponse.<PolicyResponse>builder()
                .success(true)
                .data(policy)
                .build();

        when(policyService.getPolicy(1L)).thenReturn(response);

        mockMvc.perform(get("/api/policies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // ================= PURCHASE POLICY =================

    @Test
    void purchasePolicy_shouldReturnSuccess() throws Exception {

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Policy purchased successfully")
                .build();

        when(policyService.purchasePolicy(1L)).thenReturn(response);

        mockMvc.perform(post("/api/policies/purchase/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // ================= GET USER POLICIES =================

    @Test
    void getUserPolicies_shouldReturnList() throws Exception {

        UserPolicyResponse userPolicy = UserPolicyResponse.builder()
                .policyId(1L)
                .policyName("Health")
                .build();

        ApiResponse<List<UserPolicyResponse>> response = ApiResponse.<List<UserPolicyResponse>>builder()
                .success(true)
                .data(List.of(userPolicy))
                .build();

        when(policyService.getUserPolicies()).thenReturn(response);

        mockMvc.perform(get("/api/policies/my-policies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].policyName").value("Health"));
    }

    // ================= GET ALL POLICIES =================

    @Test
    void getAllPolicies_shouldReturnList() throws Exception {

        PolicyResponse policy = new PolicyResponse();

        ApiResponse<List<PolicyResponse>> response = ApiResponse.<List<PolicyResponse>>builder()
                .success(true)
                .data(List.of(policy))
                .build();

        when(policyService.getAllPolicies()).thenReturn(response);

        mockMvc.perform(get("/api/policies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // ================= CANCEL POLICY =================

    @Test
    void cancelPolicy_shouldReturnSuccess() throws Exception {

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Policy cancelled successfully")
                .build();

        when(policyService.cancelUserPolicy(anyLong())).thenReturn(response);

        mockMvc.perform(put("/api/policies/cancel/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}