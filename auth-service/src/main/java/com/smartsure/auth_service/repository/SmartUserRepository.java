package com.smartsure.auth_service.repository;

import com.smartsure.auth_service.entity.SmartUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmartUserRepository extends JpaRepository<SmartUser, Long> {
    Optional<SmartUser> findByEmail(String email);

    boolean existsByEmail(String email);


}
