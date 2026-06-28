package com.uca.telemedicina.controller;
import com.uca.telemedicina.dto.GeneralResponse;
import com.uca.telemedicina.dto.request.*;
import com.uca.telemedicina.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController @RequestMapping("/api/doctors") @RequiredArgsConstructor @CrossOrigin("*")
public class DoctorController {
    private final DoctorService doctorService;
    @GetMapping
    public ResponseEntity<GeneralResponse> findAll() {
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(doctorService.findAll()).message("Doctores obtenidos").build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(doctorService.findById(id)).message("Doctor encontrado").build());
    }
    @GetMapping("/specialty/{specialtyId}")
    public ResponseEntity<GeneralResponse> findBySpecialty(@PathVariable Long specialtyId) {
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(doctorService.findBySpecialty(specialtyId)).message("Doctores por especialidad").build());
    }
    @PostMapping @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GeneralResponse> create(@Valid @RequestBody CreateDoctorRequest req) {
        return ResponseEntity.status(201).body(GeneralResponse.builder()
            .data(doctorService.create(req)).message("Doctor registrado exitosamente").build());
    }
    @PostMapping("/schedule") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GeneralResponse> addSchedule(@Valid @RequestBody CreateDoctorScheduleRequest req) {
        return ResponseEntity.status(201).body(GeneralResponse.builder()
            .data(doctorService.addSchedule(req)).message("Horario agregado").build());
    }
    @GetMapping("/{doctorId}/schedule")
    public ResponseEntity<GeneralResponse> getSchedule(@PathVariable Long doctorId) {
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(doctorService.getSchedule(doctorId)).message("Horario del doctor").build());
    }
    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<GeneralResponse> getAvailability(
            @PathVariable Long doctorId,
            @RequestParam String date) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(doctorService.getAvailability(doctorId, LocalDate.parse(date)))
                .message("Disponibilidad del doctor").build());
    }
}
