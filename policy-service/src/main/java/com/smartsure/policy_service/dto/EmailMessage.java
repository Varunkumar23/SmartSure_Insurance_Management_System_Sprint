package com.smartsure.policy_service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmailMessage implements Serializable {
    private String email;
    private String policyName;
}