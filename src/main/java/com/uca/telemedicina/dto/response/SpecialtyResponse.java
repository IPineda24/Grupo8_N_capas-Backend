package com.uca.telemedicina.dto.response;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SpecialtyResponse {
    private Long id;
    private String name;
    private String description;
    private Integer minAge;
    private Boolean isActive;
}
