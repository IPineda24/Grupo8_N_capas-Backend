package com.uca.telemedicina.dto.response;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MedicalNoteResponse {
    private Long id;
    private Long appointmentId;
    private String notes;
    private LocalDateTime createdAt;
}
