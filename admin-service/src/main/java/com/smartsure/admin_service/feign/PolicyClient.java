package com.smartsure.admin_service.feign;

import com.smartsure.admin_service.config.FeignConfig;
import com.smartsure.admin_service.dto.PolicyResponse;
import com.smartsure.admin_service.payload.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "POLICY-SERVICE",configuration = FeignConfig.class)
    public interface PolicyClient {

        @GetMapping("/api/policies")
        ApiResponse<List<PolicyResponse>> getAllPolicies();
    }

