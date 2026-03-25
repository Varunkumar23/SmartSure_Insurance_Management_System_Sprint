package com.smartsure.claims_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "claim_documents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long claimId;

    private String fileName;

    private String fileType;

    private String filePath;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;
}

