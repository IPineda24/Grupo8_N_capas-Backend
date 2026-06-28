package com.uca.telemedicina.repository;
import com.uca.telemedicina.entities.MedicalNote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface MedicalNoteRepository extends JpaRepository<MedicalNote, Long> {
    List<MedicalNote> findByAppointmentId(Long appointmentId);
}
