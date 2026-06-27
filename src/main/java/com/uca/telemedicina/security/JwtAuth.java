package com.uca.telemedicina.security;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uca.telemedicina.exception.ApiError;
import jakarta.servlet.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;
@Component
public class JwtAuth implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ApiError error = ApiError.builder().status(401).error("Unauthorized")
            .message("Debes autenticarte para acceder a este recurso").timestamp(LocalDateTime.now()).build();
        new ObjectMapper().findAndRegisterModules().writeValue(response.getOutputStream(), error);
    }
}
