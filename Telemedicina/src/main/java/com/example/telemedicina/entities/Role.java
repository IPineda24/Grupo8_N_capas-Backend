package com.example.telemedicina.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    private UUID id;;

    @Column(name = "role_name", nullable = false, length = 50)
    private String roleName; // ADMIN, DOCTOR, PATIENT
}