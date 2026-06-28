package com.uca.telemedicina.dto.response;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DoctorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private SpecialtyResponse specialty;
    private Integer consultationFee;
    private Boolean isActive;
}
