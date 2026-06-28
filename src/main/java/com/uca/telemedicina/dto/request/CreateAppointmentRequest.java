package com.uca.telemedicina.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data public class CreateAppointmentRequest {
    @NotNull private Long doctorId;
    @NotNull private LocalDateTime appointmentDate;
    private String stripeToken;
}
