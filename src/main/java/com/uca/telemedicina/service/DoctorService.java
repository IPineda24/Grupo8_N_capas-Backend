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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

@Service @RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SpecialtyRepository specialtyRepository;
    private final DoctorScheduleRepository scheduleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SpecialtyService specialtyService;
    private final AppointmentRepository appointmentRepository;

    public List<DoctorResponse> findAll() {
        return doctorRepository.findByIsActiveTrue().stream().map(this::toResponse).toList();
    }

    public List<DoctorResponse> findBySpecialty(Long specialtyId) {
        return doctorRepository.findBySpecialtyIdAndIsActiveTrue(specialtyId)
                .stream().map(this::toResponse).toList();
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
        User user = User.builder()
                .role(role)
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .password(passwordEncoder.encode(req.getPassword()))
                .isActive(true)
                .build();
        userRepository.save(user);
        Doctor doctor = Doctor.builder()
                .user(user)
                .specialty(specialty)
                .consultationFee(req.getConsultationFee())
                .isActive(true)
                .build();
        return toResponse(doctorRepository.save(doctor));
    }

    @Transactional
    public DoctorScheduleResponse addSchedule(CreateDoctorScheduleRequest req) {
        Doctor doctor = doctorRepository.findById(req.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado: " + req.getDoctorId()));

        if (scheduleRepository.existsByDoctorIdAndDayOfWeek(req.getDoctorId(), req.getDayOfWeek()))
            throw new BusinessRuleException("El doctor ya tiene un horario asignado para " + req.getDayOfWeek());

        DoctorSchedule schedule = DoctorSchedule.builder()
                .doctor(doctor)
                .dayOfWeek(req.getDayOfWeek())
                .shiftStart(req.getShiftStart())
                .shiftEnd(req.getShiftEnd())
                .build();
        return DoctorScheduleResponse.from(scheduleRepository.save(schedule));
    }

    public List<DoctorScheduleResponse> getSchedule(Long doctorId) {
        return scheduleRepository.findByDoctorId(doctorId)
                .stream().map(DoctorScheduleResponse::from).toList();
    }

    public DoctorResponse toResponse(Doctor d) {
        return DoctorResponse.builder()
                .id(d.getId())
                .firstName(d.getUser().getFirstName())
                .lastName(d.getUser().getLastName())
                .email(d.getUser().getEmail())
                .phone(d.getUser().getPhone())
                .specialty(specialtyService.toResponse(d.getSpecialty()))
                .consultationFee(d.getConsultationFee())
                .isActive(d.getIsActive())
                .build();
    }

    public List<AvailabilitySlotResponse> getAvailability(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado: " + doctorId));

        String dayOfWeek = date.getDayOfWeek().name();
        List<DoctorSchedule> schedules = scheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);
        if (schedules.isEmpty()) return List.of();

        List<LocalDateTime> booked = appointmentRepository
                .findBookedSlotsByDoctorAndDate(doctorId, date.atStartOfDay());

        List<AvailabilitySlotResponse> slots = new ArrayList<>();
        for (DoctorSchedule schedule : schedules) {
            LocalTime current = schedule.getShiftStart();
            while (!current.isAfter(schedule.getShiftEnd().minusMinutes(30))) {
                LocalDateTime slotDateTime = LocalDateTime.of(date, current);
                slots.add(AvailabilitySlotResponse.builder()
                        .doctorId(doctorId)
                        .doctorName(doctor.getUser().getFirstName() + " " + doctor.getUser().getLastName())
                        .slotDateTime(slotDateTime)
                        .available(!booked.contains(slotDateTime))
                        .build());
                current = current.plusMinutes(30);
            }
        }
        return slots;
    }
}