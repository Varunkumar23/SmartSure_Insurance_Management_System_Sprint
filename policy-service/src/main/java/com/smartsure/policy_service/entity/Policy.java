package com.smartsure.policy_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "policies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String policyName;

    private String description;

    private double premiumAmount;

    private int tenure;

    @Enumerated(EnumType.STRING)
    private Status status;
}