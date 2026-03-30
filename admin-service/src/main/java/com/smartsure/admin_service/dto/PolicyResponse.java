package com.smartsure.admin_service.dto;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PolicyResponse implements Serializable {

    private Long id;
    private String policyName;
    private double premiumAmount;
    private int tenure;
    private String status;
}

