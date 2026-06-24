package com.example.telemedicina.dto; // Asegúrate de que coincida con tu carpeta de DTOs

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
}