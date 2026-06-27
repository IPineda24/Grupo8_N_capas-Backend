package com.uca.telemedicina.repository;
import com.uca.telemedicina.entities.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    List<Specialty> findByIsActiveTrue();
}
