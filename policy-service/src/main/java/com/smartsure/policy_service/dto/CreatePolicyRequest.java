package com.smartsure.policy_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePolicyRequest {

    @NotBlank
    private String policyName;

    private String description;

    @Positive
    private double premiumAmount;

    private int tenure;
}
