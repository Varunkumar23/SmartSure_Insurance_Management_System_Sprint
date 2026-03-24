package com.smartsure.auth_service.dto;

import com.smartsure.auth_service.enums.Role;
import lombok.Data;

@Data
public class RegisterResponse {

    private Long id;
    private String name;
    private String email;
    private String address;
    private Role role;
}