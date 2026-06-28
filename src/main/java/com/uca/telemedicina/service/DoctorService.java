package com.uca.telemedicina.service;
import com.uca.telemedicina.dto.request.*;
import com.uca.telemedicina.dto.response.*;
import com.uca.telemedicina.entities.*;
import com.uca.telemedicina.exception.*;
import com.uca.telemedicina.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SpecialtyRepository specialtyRepository;
    private final DoctorScheduleRepository scheduleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SpecialtyService specialtyService;
    public List<DoctorResponse> findAll() {
        return doctorRepository.findByIsActiveTrue().stream().map(this::toResponse).toList();
    }
    public List<DoctorResponse> findBySpecialty(Long specialtyId) {
        return doctorRepository.findBySpecialtyIdAndIsActiveTrue(specialtyId).stream().map(this::toResponse).toList();
    }
    public DoctorResponse findById(Long id) {
        return toResponse(doctorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado: " + id)));
    }
    @Transactional
    public DoctorResponse create(CreateDoctorRequest req) {
        if (userRepository.existsByEmail(req.getEmail()))
            throw new BusinessRuleException("El email ya está en uso: " + req.getEmail());
        Role role = roleRepository.findByRoleName("ROLE_DOCTOR")
            .orElseThrow(() -> new ResourceNotFoundException("Rol ROLE_DOCTOR no encontrado"));
        Specialty specialty = specialtyRepository.findById(req.getSpecialtyId())
            .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada: " + req.getSpecialtyId()));
        User user = User.builder().role(role).firstName(req.getFirstName()).lastName(req.getLastName())
            .email(req.getEmail()).phone(req.getPhone()).password(passwordEncoder.encode(req.getPassword()))
            .isActive(true).build();
        userRepository.save(user);
        Doctor doctor = Doctor.builder().user(user).specialty(specialty)
            .consultationFee(req.getConsultationFee()).isActive(true).build();
        return toResponse(doctorRepository.save(doctor));
    }
    @Transactional
    public DoctorSchedule addSchedule(CreateDoctorScheduleRequest req) {
        Doctor doctor = doctorRepository.findById(req.getDoctorId())
            .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado: " + req.getDoctorId()));
        DoctorSchedule schedule = DoctorSchedule.builder().doctor(doctor).dayOfWeek(req.getDayOfWeek())
            .shiftStart(req.getShiftStart()).shiftEnd(req.getShiftEnd()).build();
        return scheduleRepository.save(schedule);
    }
    public List<DoctorSchedule> getSchedule(Long doctorId) {
        return scheduleRepository.findByDoctorId(doctorId);
    }
    public DoctorResponse toResponse(Doctor d) {
        return DoctorResponse.builder().id(d.getId())
            .firstName(d.getUser().getFirstName()).lastName(d.getUser().getLastName())
            .email(d.getUser().getEmail()).phone(d.getUser().getPhone())
            .specialty(specialtyService.toResponse(d.getSpecialty()))
            .consultationFee(d.getConsultationFee()).isActive(d.getIsActive()).build();
    }
}
