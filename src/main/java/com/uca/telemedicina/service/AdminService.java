package com.uca.telemedicina.service;
import com.uca.telemedicina.dto.response.DoctorReportResponse;
import com.uca.telemedicina.entities.Doctor;
import com.uca.telemedicina.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
@Service @RequiredArgsConstructor
public class AdminService {
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public List<DoctorReportResponse> getMostRequestedDoctors() {
        List<Object[]> results = doctorRepository.findMostRequestedDoctors();
        List<DoctorReportResponse> report = new ArrayList<>();
        for (Object[] row : results) {
            Doctor d = (Doctor) row[0];
            Long count = row[1] instanceof Long ? (Long) row[1] : ((Number) row[1]).longValue();
            report.add(DoctorReportResponse.builder()
                .doctorId(d.getId())
                .doctorName(d.getUser().getFirstName() + " " + d.getUser().getLastName())
                .specialty(d.getSpecialty().getName())
                .totalAppointments(count).build());
        }
        return report;
    }
}
