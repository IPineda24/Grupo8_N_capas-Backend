package com.uca.telemedicina.service;

import com.uca.telemedicina.dto.request.*;
import com.uca.telemedicina.dto.response.*;
import com.uca.telemedicina.entities.*;
import com.uca.telemedicina.enums.*;
import com.uca.telemedicina.exception.*;
import com.uca.telemedicina.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.List;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository scheduleRepository;
    private final PaymentRepository paymentRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Transactional
    public AppointmentResponse create(CreateAppointmentRequest req, String patientEmail) {
        Patient patient = patientRepository.findAll().stream()
                .filter(p -> p.getUser().getEmail().equals(patientEmail)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado para: " + patientEmail));
        Doctor doctor = doctorRepository.findById(req.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado: " + req.getDoctorId()));

        String dayOfWeek = req.getAppointmentDate().getDayOfWeek().name();
        List<DoctorSchedule> schedules = scheduleRepository.findByDoctorIdAndDayOfWeek(doctor.getId(), dayOfWeek);
        if (schedules.isEmpty())
            throw new BusinessRuleException("El doctor no atiende el día: " + dayOfWeek);

        LocalTime appointmentTime = req.getAppointmentDate().toLocalTime();
        boolean withinSchedule = schedules.stream().anyMatch(s ->
                !appointmentTime.isBefore(s.getShiftStart()) && !appointmentTime.isAfter(s.getShiftEnd()));
        if (!withinSchedule)
            throw new BusinessRuleException("La hora no está dentro del horario del doctor");

        int minutes = appointmentTime.getMinute();
        if (minutes != 0 && minutes != 30)
            throw new BusinessRuleException("Las citas solo pueden agendarse en intervalos de 30 minutos (ej: 8:00, 8:30, 9:00)");

        int patientAge = Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears();
        int minAge = doctor.getSpecialty().getMinAge();
        if (patientAge < minAge)
            throw new BusinessRuleException("El paciente no cumple la edad mínima (" + minAge + ") para esta especialidad");

        if (!appointmentRepository.findConflictingDoctorAppointments(doctor.getId(), req.getAppointmentDate()).isEmpty())
            throw new AppointmentConflictException("El doctor ya tiene una cita en ese horario");

        if (!appointmentRepository.findConflictingPatientAppointments(patient.getId(), req.getAppointmentDate()).isEmpty())
            throw new AppointmentConflictException("El paciente ya tiene una cita en ese horario");

        String meetingLink = "https://meet.telemedicina.com/" + UUID.randomUUID();
        Appointment appointment = Appointment.builder()
                .patient(patient).doctor(doctor)
                .appointmentDate(req.getAppointmentDate())
                .status(AppointmentStatus.CONFIRMED)
                .meetingLink(meetingLink).build();
        appointmentRepository.save(appointment);

        String txId = "SIM_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        if (req.getStripeToken() != null && !req.getStripeToken().isBlank())
            txId = "STRIPE_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Payment payment = Payment.builder()
                .appointment(appointment)
                .amountCents(doctor.getConsultationFee() * 100)
                .status(PaymentStatus.COMPLETED)
                .stripeTransactionId(txId).build();
        paymentRepository.save(payment);

        return toResponse(appointment);
    }

    @Transactional
    public AppointmentResponse complete(Long appointmentId, String doctorEmail) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada: " + appointmentId));
        if (!appointment.getDoctor().getUser().getEmail().equals(doctorEmail))
            throw new BusinessRuleException("Solo el doctor de la cita puede completarla");
        if (appointment.getStatus() == AppointmentStatus.CANCELLED)
            throw new BusinessRuleException("No se puede completar una cita cancelada");
        if (appointment.getStatus() == AppointmentStatus.COMPLETED)
            throw new BusinessRuleException("La cita ya está completada");
        appointment.setStatus(AppointmentStatus.COMPLETED);
        return toResponse(appointmentRepository.save(appointment));
    }

    @Transactional
    public AppointmentResponse cancel(Long appointmentId, String requesterEmail) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada: " + appointmentId));
        String patientEmail = appointment.getPatient().getUser().getEmail();
        String doctorEmail = appointment.getDoctor().getUser().getEmail();
        boolean isOwner = patientEmail.equals(requesterEmail) || doctorEmail.equals(requesterEmail);
        if (!isOwner)
            throw new BusinessRuleException("No tienes permiso para cancelar esta cita");
        if (appointment.getStatus() == AppointmentStatus.CANCELLED)
            throw new BusinessRuleException("La cita ya está cancelada");
        if (appointment.getStatus() == AppointmentStatus.COMPLETED)
            throw new BusinessRuleException("No se puede cancelar una cita completada");

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        paymentRepository.findByAppointmentId(appointmentId).ifPresent(payment -> {
            long hoursUntil = Duration.between(LocalDateTime.now(), appointment.getAppointmentDate()).toHours();
            if (hoursUntil >= 4) {
                payment.setStatus(PaymentStatus.REFUNDED);
                payment.setStripeTransactionId("REFUND_80%_" + payment.getStripeTransactionId());
            } else {
                payment.setStatus(PaymentStatus.FAILED);
            }
            paymentRepository.save(payment);
        });
        return toResponse(appointment);
    }

    @Transactional
    public AppointmentResponse rate(Long appointmentId, RateAppointmentRequest req, String patientEmail) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada: " + appointmentId));
        if (!appointment.getPatient().getUser().getEmail().equals(patientEmail))
            throw new BusinessRuleException("Solo el paciente puede calificar la cita");
        if (appointment.getStatus() != AppointmentStatus.COMPLETED)
            throw new BusinessRuleException("Solo se pueden calificar citas completadas");
        if (appointment.getRating() != null)
            throw new BusinessRuleException("Esta cita ya fue calificada");
        appointment.setRating(req.getRating());
        return toResponse(appointmentRepository.save(appointment));
    }

    public List<AppointmentResponse> findByPatientEmail(String email) {
        return patientRepository.findAll().stream()
                .filter(p -> p.getUser().getEmail().equals(email)).findFirst()
                .map(p -> appointmentRepository.findByPatientId(p.getId()).stream().map(this::toResponse).toList())
                .orElse(List.of());
    }

    public List<AppointmentResponse> findByDoctorEmail(String email) {
        return doctorRepository.findAll().stream()
                .filter(d -> d.getUser().getEmail().equals(email)).findFirst()
                .map(d -> appointmentRepository.findByDoctorId(d.getId()).stream().map(this::toResponse).toList())
                .orElse(List.of());
    }

    public List<AppointmentResponse> findAll() {
        return appointmentRepository.findAll().stream().map(this::toResponse).toList();
    }

    public AppointmentResponse toResponse(Appointment a) {
        return AppointmentResponse.builder()
                .id(a.getId())
                .patient(patientService.toResponse(a.getPatient()))
                .doctor(doctorService.toResponse(a.getDoctor()))
                .appointmentDate(a.getAppointmentDate())
                .status(a.getStatus())
                .meetingLink(a.getMeetingLink())
                .rating(a.getRating())
                .createdAt(a.getCreatedAt())
                .build();
    }
}