package com.example.telemedicina.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class UserResponseDto {
    private UUID id; // Adaptado a tu arquitectura de UUID
    private String email;
    private String role;
}