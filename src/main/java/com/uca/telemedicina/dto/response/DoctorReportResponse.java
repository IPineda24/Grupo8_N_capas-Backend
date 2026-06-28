package com.uca.telemedicina.dto.response;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DoctorReportResponse {
    private Long doctorId;
    private String doctorName;
    private String specialty;
    private Long totalAppointments;
}
