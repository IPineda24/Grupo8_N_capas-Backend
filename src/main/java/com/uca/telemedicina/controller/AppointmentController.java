package com.uca.telemedicina.controller;
import com.uca.telemedicina.dto.GeneralResponse;
import com.uca.telemedicina.dto.request.*;
import com.uca.telemedicina.entities.User;
import com.uca.telemedicina.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/appointments") @RequiredArgsConstructor @CrossOrigin("*")
public class AppointmentController {
    private final AppointmentService appointmentService;
    @PostMapping @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<GeneralResponse> create(@Valid @RequestBody CreateAppointmentRequest req,
                                                   @AuthenticationPrincipal User user) {
        return ResponseEntity.status(201).body(GeneralResponse.builder()
            .data(appointmentService.create(req, user.getEmail())).message("Cita creada exitosamente").build());
    }
    @GetMapping @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GeneralResponse> findAll() {
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(appointmentService.findAll()).message("Citas obtenidas").build());
    }
    @GetMapping("/my") @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<GeneralResponse> myAppointments(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(appointmentService.findByPatientEmail(user.getEmail())).message("Mis citas").build());
    }
    @GetMapping("/doctor-agenda") @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<GeneralResponse> doctorAgenda(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(appointmentService.findByDoctorEmail(user.getEmail())).message("Agenda del doctor").build());
    }
    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<GeneralResponse> cancel(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(appointmentService.cancel(id, user.getEmail())).message("Cita cancelada").build());
    }
    @PutMapping("/{id}/rate") @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<GeneralResponse> rate(@PathVariable Long id,
                                                @Valid @RequestBody RateAppointmentRequest req,
                                                @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(appointmentService.rate(id, req, user.getEmail())).message("Cita calificada").build());
    }
}
