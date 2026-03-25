package com.smartsure.policy_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_policies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String userEmail;

    private Long policyId;

    private LocalDateTime purchaseDate;

    @Enumerated(EnumType.STRING)
    private Status status;
}