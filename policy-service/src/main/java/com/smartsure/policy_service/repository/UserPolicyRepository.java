package com.smartsure.policy_service.repository;

import com.smartsure.policy_service.entity.UserPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPolicyRepository extends JpaRepository<UserPolicy,Long> {

    List<UserPolicy> findByUserId(Long userId);

    List<UserPolicy> findByUserEmail(String email);
}
