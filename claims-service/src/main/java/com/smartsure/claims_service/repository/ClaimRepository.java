package com.smartsure.claims_service.repository;

import com.smartsure.claims_service.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
}
