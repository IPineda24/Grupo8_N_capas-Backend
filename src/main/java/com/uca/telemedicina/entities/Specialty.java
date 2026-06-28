package com.uca.telemedicina.entities;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="specialties") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Specialty {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="name", nullable=false) private String name;
    @Column(name="description", columnDefinition="TEXT") private String description;
    @Column(name="min_age", nullable=false) @Builder.Default private Integer minAge = 0;
    @Column(name="is_active", nullable=false) @Builder.Default private Boolean isActive = true;
}
