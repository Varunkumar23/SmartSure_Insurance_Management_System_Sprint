package com.smartsure.claims_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClaimResponse {

    private Long id;
    private Long policyId;
    private String description;
    private String status;
}