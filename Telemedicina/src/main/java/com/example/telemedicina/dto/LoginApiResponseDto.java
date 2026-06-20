package com.example.telemedicina.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginApiResponseDto {
    private String description;
    private LoginResponseBodyDto body;
}