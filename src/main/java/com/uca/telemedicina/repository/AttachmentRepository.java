package com.uca.telemedicina.repository;
import com.uca.telemedicina.entities.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByAppointmentId(Long appointmentId);
}
