package com.uca.telemedicina.dto.response;

import lombok.*;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PatientHistoryResponse {
    private PatientResponse patient;
    private List<AppointmentHistoryEntry> appointments;
    private Integer totalAppointments;
    private Integer completedAppointments;
    private Integer cancelledAppointments;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class AppointmentHistoryEntry {
        private AppointmentResponse appointment;
        private List<PrescriptionResponse> prescriptions;
        private List<MedicalNoteResponse> medicalNotes;
    }
}