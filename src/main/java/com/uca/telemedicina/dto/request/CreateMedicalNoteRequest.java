package com.uca.telemedicina.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data public class CreateMedicalNoteRequest {
    @NotNull private Long appointmentId;
    @NotBlank private String notes;
}
