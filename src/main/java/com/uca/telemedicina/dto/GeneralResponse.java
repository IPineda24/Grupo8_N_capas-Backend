package com.uca.telemedicina.dto;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class GeneralResponse {
    private Object data;
    private String message;
}
