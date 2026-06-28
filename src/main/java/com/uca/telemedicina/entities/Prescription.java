package com.uca.telemedicina.entities;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="prescriptions") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Prescription {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="appointment_id", nullable=false) private Appointment appointment;
    @Column(name="medication_details", columnDefinition="TEXT", nullable=false) private String medicationDetails;
    @Column(name="hash_signature", nullable=false) private String hashSignature;
    @Column(name="uses_remaining", nullable=false) @Builder.Default private Integer usesRemaining = 3;
    @Column(name="expiration_date", nullable=false) private LocalDateTime expirationDate;
    @Column(name="created_at") private LocalDateTime createdAt;
    @Column(name="updated_at") private LocalDateTime updatedAt;
    @PrePersist public void prePersist() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate public void preUpdate() { updatedAt = LocalDateTime.now(); }
}
