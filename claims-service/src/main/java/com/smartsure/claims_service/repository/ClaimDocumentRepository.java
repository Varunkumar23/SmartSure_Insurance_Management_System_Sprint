package com.smartsure.claims_service.repository;

import com.smartsure.claims_service.entity.ClaimDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimDocumentRepository extends JpaRepository<ClaimDocument, Long> {
}
