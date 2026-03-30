package com.smartsure.admin_service.dto;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClaimResponse implements Serializable {

    private Long id;
    private Long policyId;
    private String description;
    private String status;
}
