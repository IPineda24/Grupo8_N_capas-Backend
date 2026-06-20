package com.example.telemedicina.dto;

import com.example.telemedicina.dto.ResponseBodyDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseDto {
    private String description;
    private ResponseBodyDto body;
}