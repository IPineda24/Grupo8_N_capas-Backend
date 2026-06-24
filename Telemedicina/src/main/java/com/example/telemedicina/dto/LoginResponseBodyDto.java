package com.example.telemedicina.dto;

import com.example.telemedicina.dto.LoginUserResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseBodyDto {
    private String status;
    private String token;
    private LoginUserResponseDto user;
}