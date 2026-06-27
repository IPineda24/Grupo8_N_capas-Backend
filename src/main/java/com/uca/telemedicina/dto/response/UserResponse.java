package com.uca.telemedicina.dto.response;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String role;
    private Boolean isActive;
}
