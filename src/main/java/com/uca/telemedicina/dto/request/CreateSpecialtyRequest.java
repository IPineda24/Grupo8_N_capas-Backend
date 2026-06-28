package com.uca.telemedicina.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data public class CreateSpecialtyRequest {
    @NotBlank private String name;
    private String description;
    @NotNull @Min(0) private Integer minAge;
}
