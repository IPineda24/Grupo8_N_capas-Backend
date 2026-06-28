package com.uca.telemedicina.controller;

import com.uca.telemedicina.dto.GeneralResponse;
import com.uca.telemedicina.dto.request.UpdatePatientRequest;
import com.uca.telemedicina.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GeneralResponse> findAll() {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(patientService.findAll()).message("Pacientes obtenidos").build());
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<GeneralResponse> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(patientService.findByEmail(userDetails.getUsername())).message("Perfil obtenido").build());
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<GeneralResponse> update(
            @RequestBody UpdatePatientRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(patientService.update(userDetails.getUsername(), req))
                .message("Perfil actualizado").build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<GeneralResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(patientService.findById(id)).message("Paciente encontrado").build());
    }
}