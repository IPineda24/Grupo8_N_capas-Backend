package com.uca.telemedicina.dto.response;
import com.uca.telemedicina.enums.PaymentStatus;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long appointmentId;
    private Integer amountCents;
    private PaymentStatus status;
    private String stripeTransactionId;
    private LocalDateTime createdAt;
}
