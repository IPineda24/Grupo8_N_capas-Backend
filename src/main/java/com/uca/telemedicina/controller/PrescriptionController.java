package com.uca.telemedicina.controller;
import com.uca.telemedicina.dto.GeneralResponse;
import com.uca.telemedicina.dto.request.CreatePrescriptionRequest;
import com.uca.telemedicina.entities.User;
import com.uca.telemedicina.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/prescriptions") @RequiredArgsConstructor @CrossOrigin("*")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;
    @PostMapping @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<GeneralResponse> create(@Valid @RequestBody CreatePrescriptionRequest req,
                                                   @AuthenticationPrincipal User user) {
        return ResponseEntity.status(201).body(GeneralResponse.builder()
            .data(prescriptionService.create(req, user.getEmail())).message("Receta emitida").build());
    }
    @PutMapping("/{id}/use") @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<GeneralResponse> use(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(prescriptionService.usePrescription(id, user.getEmail())).message("Receta usada").build());
    }
    @GetMapping("/my") @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<GeneralResponse> myPrescriptions(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(prescriptionService.findByPatientEmail(user.getEmail())).message("Mis recetas").build());
    }
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<GeneralResponse> byAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(prescriptionService.findByAppointment(appointmentId)).message("Recetas de la cita").build());
    }
}
