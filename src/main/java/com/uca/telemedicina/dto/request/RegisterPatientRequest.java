package com.uca.telemedicina.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data public class RegisterPatientRequest {
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @NotBlank @Email private String email;
    private String phone;
    @NotBlank @Size(min=8) private String password;
    @NotNull private LocalDate dateOfBirth;
    private BigDecimal weightLbs;
    private BigDecimal heightCm;
}
