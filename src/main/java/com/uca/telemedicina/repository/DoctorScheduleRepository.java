package com.uca.telemedicina.repository;

import com.uca.telemedicina.entities.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    List<DoctorSchedule> findByDoctorId(Long doctorId);
    List<DoctorSchedule> findByDoctorIdAndDayOfWeek(Long doctorId, String dayOfWeek);
    boolean existsByDoctorIdAndDayOfWeek(Long doctorId, String dayOfWeek);
}