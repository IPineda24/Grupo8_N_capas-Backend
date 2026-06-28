package com.uca.telemedicina.dto.response;
import com.uca.telemedicina.enums.AppointmentStatus;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private PatientResponse patient;
    private DoctorResponse doctor;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private String meetingLink;
    private Integer rating;
    private LocalDateTime createdAt;
}
