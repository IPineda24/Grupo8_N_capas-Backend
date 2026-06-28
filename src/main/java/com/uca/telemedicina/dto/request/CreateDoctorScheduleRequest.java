package com.uca.telemedicina.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalTime;
@Data public class CreateDoctorScheduleRequest {
    @NotNull private Long doctorId;
    @NotBlank private String dayOfWeek;
    @NotNull private LocalTime shiftStart;
    @NotNull private LocalTime shiftEnd;
}
