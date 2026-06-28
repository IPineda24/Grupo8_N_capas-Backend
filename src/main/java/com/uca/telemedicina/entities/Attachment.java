package com.uca.telemedicina.entities;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="attachments") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Attachment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="appointment_id", nullable=false) private Appointment appointment;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="uploaded_by", nullable=false) private User uploadedBy;
    @Column(name="document_type") private String documentType;
    @Column(name="file_name", nullable=false) private String fileName;
    @Column(name="file_url", nullable=false) private String fileUrl;
    @Column(name="created_at") private LocalDateTime createdAt;
    @PrePersist public void prePersist() { createdAt = LocalDateTime.now(); }
}
