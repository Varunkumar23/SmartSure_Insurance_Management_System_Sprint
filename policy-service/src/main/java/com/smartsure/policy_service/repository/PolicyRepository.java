package com.smartsure.policy_service.repository;

import com.smartsure.policy_service.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy,Long> {
}
