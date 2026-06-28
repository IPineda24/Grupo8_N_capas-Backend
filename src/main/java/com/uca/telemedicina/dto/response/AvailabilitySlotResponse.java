package com.uca.telemedicina.dto.response;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AvailabilitySlotResponse {
    private Long doctorId;
    private String doctorName;
    private LocalDateTime slotDateTime;
    private Boolean available;
}
