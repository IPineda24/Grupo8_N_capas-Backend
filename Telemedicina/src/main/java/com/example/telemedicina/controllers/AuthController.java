package com.example.telemedicina.controllers;

import com.example.telemedicina.dto.LoginRequest;
import com.example.telemedicina.dto.RegisterRequest;
import com.example.telemedicina.dto.*; // Importamos tus nuevos DTOs estructurados
import com.example.telemedicina.entities.User;
import com.example.telemedicina.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> register(@RequestBody RegisterRequest request) {
        // 1. Registramos al usuario y obtenemos la entidad guardada completa
        User usuarioGuardado = authService.register(request);

        // 2. Construimos la respuesta anidada para el Registro
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(usuarioGuardado.getId());
        userDto.setEmail(usuarioGuardado.getEmail());
        userDto.setRole(usuarioGuardado.getRole().getRoleName());

        ResponseBodyDto bodyDto = new ResponseBodyDto();
        bodyDto.setStatus("success");
        bodyDto.setMessage("Cuenta creada con éxito");
        bodyDto.setUser(userDto);

        ApiResponseDto response = new ApiResponseDto();
        response.setDescription("Usuario registrado exitosamente");
        response.setBody(bodyDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginApiResponseDto> login(@RequestBody LoginRequest request) {
        // 1. Autenticamos las credenciales y obtenemos el usuario completo
        User usuarioAutenticado = authService.login(request);

        // 2. Generamos el token a partir de ese usuario obtenido
        String token = authService.generateToken(usuarioAutenticado);

        // 3. Construimos la respuesta anidada para el Login
        LoginUserResponseDto userDto = new LoginUserResponseDto();
        userDto.setId(usuarioAutenticado.getId());
        userDto.setFirstName(usuarioAutenticado.getFirstName());
        userDto.setLastName(usuarioAutenticado.getLastName());
        userDto.setRole(usuarioAutenticado.getRole().getRoleName());

        LoginResponseBodyDto bodyDto = new LoginResponseBodyDto();
        bodyDto.setStatus("success");
        bodyDto.setToken(token);
        bodyDto.setUser(userDto);

        LoginApiResponseDto response = new LoginApiResponseDto();
        response.setDescription("Login exitoso");
        response.setBody(bodyDto);

        return ResponseEntity.ok(response);
    }
}