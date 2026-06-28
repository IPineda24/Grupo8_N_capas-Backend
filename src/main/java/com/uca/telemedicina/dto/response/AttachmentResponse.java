package com.uca.telemedicina.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AttachmentResponse {
    private Long id;
    private Long appointmentId;
    private String uploadedBy;
    private String documentType;
    private String fileName;
    private String fileUrl;
    private LocalDateTime createdAt;
}