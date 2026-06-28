package com.uca.telemedicina.controller;

import com.uca.telemedicina.dto.GeneralResponse;
import com.uca.telemedicina.entities.User;
import com.uca.telemedicina.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PostMapping("/{id}/attachments")
    public ResponseEntity<GeneralResponse> upload(
            @PathVariable Long id,
            @RequestParam String fileName,
            @RequestParam String fileUrl,
            @RequestParam(required = false) String documentType,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(201).body(GeneralResponse.builder()
                .data(attachmentService.upload(id, user.getEmail(), fileName, fileUrl, documentType))
                .message("Documento adjuntado").build());
    }

    @GetMapping("/{id}/attachments")
    public ResponseEntity<GeneralResponse> findByAppointment(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(attachmentService.findByAppointment(id, user.getEmail()))
                .message("Adjuntos de la cita").build());
    }
}