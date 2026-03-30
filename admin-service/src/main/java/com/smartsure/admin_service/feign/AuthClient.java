package com.smartsure.admin_service.feign;

import com.smartsure.admin_service.config.FeignConfig;
import com.smartsure.admin_service.dto.UserResponse;
import com.smartsure.admin_service.payload.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "AUTH-SERVICE",configuration = FeignConfig.class)
public interface AuthClient {

    @GetMapping("/api/auth/getAllUsers")
    ApiResponse<List<UserResponse>> getAllUsers();
}

