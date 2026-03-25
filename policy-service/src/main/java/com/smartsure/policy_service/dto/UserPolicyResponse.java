package com.smartsure.policy_service.dto;

import com.smartsure.policy_service.entity.Status;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPolicyResponse {

    private Long policyId;
    private String policyName;
    private double premiumAmount;
    private Status status;
}