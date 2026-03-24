package com.smartsure.policy_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PolicyResponse {

    private Long id;
    private String policyName;
    private double premiumAmount;
    private int tenure;
    private String status;
}
