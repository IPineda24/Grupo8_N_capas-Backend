package com.uca.telemedicina.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdatePatientRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private BigDecimal weightLbs;
    private BigDecimal heightCm;
}