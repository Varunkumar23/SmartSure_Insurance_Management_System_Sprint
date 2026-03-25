package com.smartsure.claims_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitiateClaimRequest {
    private Long policyId;
    private String description;
}
