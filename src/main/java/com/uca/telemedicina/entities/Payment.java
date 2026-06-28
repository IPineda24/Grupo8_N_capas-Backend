package com.uca.telemedicina.entities;
import com.uca.telemedicina.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="payments") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Payment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="appointment_id", nullable=false) private Appointment appointment;
    @Column(name="amount_cents", nullable=false) private Integer amountCents;
    @Enumerated(EnumType.STRING) @Column(name="status", nullable=false) @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;
    @Column(name="stripe_transaction_id") private String stripeTransactionId;
    @Column(name="created_at") private LocalDateTime createdAt;
    @Column(name="updated_at") private LocalDateTime updatedAt;
    @PrePersist public void prePersist() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate public void preUpdate() { updatedAt = LocalDateTime.now(); }
}
