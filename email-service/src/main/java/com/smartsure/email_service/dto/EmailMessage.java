package com.smartsure.email_service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmailMessage implements Serializable {
    private String email;
    private String policyName;
}
