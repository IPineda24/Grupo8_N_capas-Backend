package com.uca.telemedicina.dto.response;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class JwtAuthResponse {
    private String accessToken;
    @Builder.Default private String tokenType = "Bearer";
    private UserResponse user;
}
