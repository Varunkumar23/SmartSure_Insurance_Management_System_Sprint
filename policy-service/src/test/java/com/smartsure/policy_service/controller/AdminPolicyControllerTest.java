package com.smartsure.policy_service.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartsure.policy_service.dto.CreatePolicyRequest;
import com.smartsure.policy_service.dto.PolicyResponse;
import com.smartsure.policy_service.payload.ApiResponse;
import com.smartsure.policy_service.security.JwtAuthenticationFilter;
import com.smartsure.policy_service.security.JwtUtil;
import com.smartsure.policy_service.service.PolicyService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminPolicyController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminPolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PolicyService policyService;

    // 🔥 REQUIRED (because of security layer)
    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    // ================= CREATE POLICY =================

    @Test
    void createPolicy_shouldReturnSuccess() throws Exception {

        CreatePolicyRequest request = new CreatePolicyRequest();
        request.setPolicyName("Health");
        request.setTenure(12);
        request.setPremiumAmount(5000.0);

        PolicyResponse policy = new PolicyResponse();

        ApiResponse<PolicyResponse> response = ApiResponse.<PolicyResponse>builder()
                .success(true)
                .data(policy)
                .build();

        when(policyService.createPolicy(any())).thenReturn(response);

        mockMvc.perform(post("/api/admin/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // ================= UPDATE POLICY =================

    @Test
    void updatePolicy_shouldReturnSuccess() throws Exception {

        CreatePolicyRequest request = new CreatePolicyRequest();
        request.setPolicyName("Updated");

        PolicyResponse policy = new PolicyResponse();

        ApiResponse<PolicyResponse> response = ApiResponse.<PolicyResponse>builder()
                .success(true)
                .data(policy)
                .build();

        when(policyService.updatePolicy(anyLong(), any())).thenReturn(response);

        mockMvc.perform(put("/api/admin/policies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // ================= DELETE POLICY =================

    @Test
    void deletePolicy_shouldReturnSuccess() throws Exception {

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Deleted successfully")
                .build();

        when(policyService.deletePolicy(1L)).thenReturn(response);

        mockMvc.perform(delete("/api/admin/policies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // ================= EXPIRE POLICY =================

    @Test
    void expirePolicy_shouldReturnSuccess() throws Exception {

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Expired successfully")
                .build();

        when(policyService.expirePolicy(1L)).thenReturn(response);

        mockMvc.perform(put("/api/admin/policies/1/expire"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
