package com.smartsure.admin_service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserResponse implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String role;
}
