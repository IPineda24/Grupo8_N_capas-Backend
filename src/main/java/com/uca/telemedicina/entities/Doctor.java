package com.uca.telemedicina.entities;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="doctors") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Doctor {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id", nullable=false, unique=true) private User user;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="specialty_id", nullable=false) private Specialty specialty;
    @Column(name="consultation_fee", nullable=false) @Builder.Default private Integer consultationFee = 0;
    @Column(name="is_active", nullable=false) @Builder.Default private Boolean isActive = true;
}
