package com.uca.telemedicina.entities;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="roles") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Role {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="role_name", nullable=false) private String roleName;
}
