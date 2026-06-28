package com.uca.telemedicina.dto.request;
import lombok.Data;
@Data public class UpdateSpecialtyRequest {
    private String name;
    private String description;
    private Integer minAge;
    private Boolean isActive;
}
