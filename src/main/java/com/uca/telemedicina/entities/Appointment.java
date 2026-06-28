package com.uca.telemedicina.entities;
import com.uca.telemedicina.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="appointments") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Appointment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="patient_id", nullable=false) private Patient patient;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="doctor_id", nullable=false) private Doctor doctor;
    @Column(name="appointment_date", nullable=false) private LocalDateTime appointmentDate;
    @Enumerated(EnumType.STRING) @Column(name="status", nullable=false) @Builder.Default
    private AppointmentStatus status = AppointmentStatus.PENDING;
    @Column(name="meeting_link") private String meetingLink;
    @Column(name="rating") private Integer rating;
    @Version @Column(name="version") private Integer version;
    @Column(name="created_at") private LocalDateTime createdAt;
    @Column(name="updated_at") private LocalDateTime updatedAt;
    @PrePersist public void prePersist() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate public void preUpdate() { updatedAt = LocalDateTime.now(); }
}
