package com.uca.telemedicina.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data public class CreatePrescriptionRequest {
    @NotNull private Long appointmentId;
    @NotBlank private String medicationDetails;
    @NotNull private LocalDateTime expirationDate;
}
