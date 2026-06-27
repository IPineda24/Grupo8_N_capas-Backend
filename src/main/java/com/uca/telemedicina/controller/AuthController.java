package com.uca.telemedicina.controller;
import com.uca.telemedicina.dto.GeneralResponse;
import com.uca.telemedicina.dto.request.*;
import com.uca.telemedicina.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor @CrossOrigin("*")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<GeneralResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(GeneralResponse.builder()
            .data(authService.login(req)).message("Login exitoso").build());
    }
    @PostMapping("/register")
    public ResponseEntity<GeneralResponse> register(@Valid @RequestBody RegisterPatientRequest req) {
        return ResponseEntity.status(201).body(GeneralResponse.builder()
            .data(authService.register(req)).message("Paciente registrado exitosamente").build());
    }
}
