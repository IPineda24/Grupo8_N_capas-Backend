package com.uca.telemedicina.controller;
import com.uca.telemedicina.dto.GeneralResponse;
import com.uca.telemedicina.dto.request.*;
import com.uca.telemedicina.service.SpecialtyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/specialties") @RequiredArgsConstructor @CrossOrigin("*")
public class SpecialtyController {
    private final SpecialtyService specialtyService;
    @GetMapping
    public ResponseEntity<GeneralResponse> findAll() {
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(specialtyService.findAll()).message("Especialidades obtenidas").build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(specialtyService.findById(id)).message("Especialidad encontrada").build());
    }
    @PostMapping @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GeneralResponse> create(@Valid @RequestBody CreateSpecialtyRequest req) {
        return ResponseEntity.status(201).body(GeneralResponse.builder()
            .data(specialtyService.create(req)).message("Especialidad creada").build());
    }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GeneralResponse> update(@PathVariable Long id, @RequestBody UpdateSpecialtyRequest req) {
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(specialtyService.update(id, req)).message("Especialidad actualizada").build());
    }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GeneralResponse> deactivate(@PathVariable Long id) {
        specialtyService.deactivate(id);
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(null).message("Especialidad desactivada").build());
    }
}
