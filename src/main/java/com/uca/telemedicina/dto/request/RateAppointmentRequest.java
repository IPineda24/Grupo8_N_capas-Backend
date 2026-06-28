package com.uca.telemedicina.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data public class RateAppointmentRequest {
    @NotNull @Min(1) @Max(5) private Integer rating;
}
