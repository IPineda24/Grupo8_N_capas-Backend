package com.uca.telemedicina.repository;
import com.uca.telemedicina.entities.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByAppointmentId(Long appointmentId);
    List<Prescription> findByAppointmentPatientId(Long patientId);
}
