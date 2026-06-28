package com.uca.telemedicina.controller;
import com.uca.telemedicina.dto.GeneralResponse;
import com.uca.telemedicina.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/admin") @RequiredArgsConstructor @CrossOrigin("*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;
    @GetMapping("/reports/doctors")
    public ResponseEntity<GeneralResponse> mostRequestedDoctors() {
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(adminService.getMostRequestedDoctors()).message("Reporte de doctores más solicitados").build());
    }
}
