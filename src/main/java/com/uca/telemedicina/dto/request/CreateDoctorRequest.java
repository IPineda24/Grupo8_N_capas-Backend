package com.uca.telemedicina.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data public class CreateDoctorRequest {
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @NotBlank @Email private String email;
    private String phone;
    @NotBlank @Size(min=8) private String password;
    @NotNull private Long specialtyId;
    @NotNull @Min(0) private Integer consultationFee;
}
