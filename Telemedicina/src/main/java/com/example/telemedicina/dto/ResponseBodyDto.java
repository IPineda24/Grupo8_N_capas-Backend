package com.example.telemedicina.dto;

import com.example.telemedicina.dto.UserResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseBodyDto {
    private String status;
    private String message;
    private UserResponseDto user;
}