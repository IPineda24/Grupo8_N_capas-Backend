package com.uca.telemedicina.entities;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Entity @Table(name="patients") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Patient {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id", nullable=false, unique=true) private User user;
    @Column(name="date_of_birth", nullable=false) private LocalDate dateOfBirth;
    @Column(name="weight_lbs", precision=6, scale=2) private BigDecimal weightLbs;
    @Column(name="height_cm", precision=5, scale=2) private BigDecimal heightCm;
    @Column(name="is_active", nullable=false) @Builder.Default private Boolean isActive = true;
}
