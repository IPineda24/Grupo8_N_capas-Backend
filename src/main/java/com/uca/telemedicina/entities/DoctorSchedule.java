package com.uca.telemedicina.entities;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
@Entity @Table(name="doctors_schedule") @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DoctorSchedule {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="doctor_id", nullable=false) private Doctor doctor;
    @Column(name="day_of_week", nullable=false) private String dayOfWeek;
    @Column(name="shift_start", nullable=false) private LocalTime shiftStart;
    @Column(name="shift_end", nullable=false) private LocalTime shiftEnd;
}
