package com.uca.telemedicina.repository;
import com.uca.telemedicina.entities.Appointment;
import com.uca.telemedicina.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByDoctorId(Long doctorId);
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
           "AND a.appointmentDate = :date AND a.status NOT IN ('CANCELLED','NO_SHOW')")
    List<Appointment> findConflictingDoctorAppointments(@Param("doctorId") Long doctorId,
                                                         @Param("date") LocalDateTime date);
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId " +
           "AND a.appointmentDate = :date AND a.status NOT IN ('CANCELLED','NO_SHOW')")
    List<Appointment> findConflictingPatientAppointments(@Param("patientId") Long patientId,
                                                          @Param("date") LocalDateTime date);
    List<Appointment> findByPatientIdAndStatus(Long patientId, AppointmentStatus status);
}
