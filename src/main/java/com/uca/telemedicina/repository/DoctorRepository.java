package com.uca.telemedicina.repository;
import com.uca.telemedicina.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserId(Long userId);
    List<Doctor> findByIsActiveTrue();
    List<Doctor> findBySpecialtyIdAndIsActiveTrue(Long specialtyId);
    @Query("SELECT d FROM Doctor d WHERE d.isActive = true AND d.specialty.id = :specialtyId")
    List<Doctor> findActiveBySpecialty(@Param("specialtyId") Long specialtyId);
    @Query("SELECT d, COUNT(a) AS cnt FROM Doctor d LEFT JOIN Appointment a ON a.doctor = d " +
           "WHERE a.status = 'COMPLETED' GROUP BY d ORDER BY cnt DESC")
    List<Object[]> findMostRequestedDoctors();
}
