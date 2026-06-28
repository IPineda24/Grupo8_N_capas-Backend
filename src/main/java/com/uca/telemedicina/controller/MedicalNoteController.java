package com.uca.telemedicina.controller;
import com.uca.telemedicina.dto.GeneralResponse;
import com.uca.telemedicina.dto.request.CreateMedicalNoteRequest;
import com.uca.telemedicina.entities.User;
import com.uca.telemedicina.service.MedicalNoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/medical-notes") @RequiredArgsConstructor @CrossOrigin("*")
public class MedicalNoteController {
    private final MedicalNoteService noteService;
    @PostMapping @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<GeneralResponse> create(@Valid @RequestBody CreateMedicalNoteRequest req,
                                                   @AuthenticationPrincipal User user) {
        return ResponseEntity.status(201).body(GeneralResponse.builder()
            .data(noteService.create(req, user.getEmail())).message("Nota médica guardada").build());
    }
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<GeneralResponse> byAppointment(@PathVariable Long appointmentId,
                                                          @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(noteService.findByAppointment(appointmentId, user.getEmail()))
            .message("Notas de la cita").build());
    }
}
