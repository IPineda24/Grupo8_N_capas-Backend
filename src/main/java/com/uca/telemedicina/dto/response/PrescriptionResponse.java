package com.uca.telemedicina.dto.response;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PrescriptionResponse {
    private Long id;
    private Long appointmentId;
    private String medicationDetails;
    private String hashSignature;
    private Integer usesRemaining;
    private LocalDateTime expirationDate;
    private LocalDateTime createdAt;
}
