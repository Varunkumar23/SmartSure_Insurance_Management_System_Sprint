package com.smartsure.policy_service.feigns;


import com.smartsure.policy_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "AUTH-SERVICE")
public interface AuthClient {

    @GetMapping("/api/auth/user")
    UserResponse getUserByEmail(@RequestParam String email);
}

