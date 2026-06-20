package com.example.telemedicina.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class LoginUserResponseDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String role;
}