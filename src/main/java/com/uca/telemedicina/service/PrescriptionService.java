package com.uca.telemedicina.service;
import com.uca.telemedicina.dto.request.CreatePrescriptionRequest;
import com.uca.telemedicina.dto.response.PrescriptionResponse;
import com.uca.telemedicina.entities.*;
import com.uca.telemedicina.enums.AppointmentStatus;
import com.uca.telemedicina.exception.*;
import com.uca.telemedicina.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.*;
@Service @RequiredArgsConstructor
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public PrescriptionResponse create(CreatePrescriptionRequest req, String doctorEmail) {
        Appointment appointment = appointmentRepository.findById(req.getAppointmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada: " + req.getAppointmentId()));
        if (!appointment.getDoctor().getUser().getEmail().equals(doctorEmail))
            throw new BusinessRuleException("Solo el médico de la cita puede emitir recetas");
        if (appointment.getStatus() != AppointmentStatus.CONFIRMED && appointment.getStatus() != AppointmentStatus.COMPLETED)
            throw new BusinessRuleException("Solo se pueden emitir recetas para citas confirmadas o completadas");
        String hash = generateHash(req.getMedicationDetails() + appointment.getId() + LocalDateTime.now());
        Prescription prescription = Prescription.builder().appointment(appointment)
            .medicationDetails(req.getMedicationDetails()).hashSignature(hash)
            .usesRemaining(3).expirationDate(req.getExpirationDate()).build();
        return toResponse(prescriptionRepository.save(prescription));
    }

    @Transactional
    public PrescriptionResponse usePrescription(Long prescriptionId, String requesterEmail) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
            .orElseThrow(() -> new ResourceNotFoundException("Receta no encontrada: " + prescriptionId));
        String patientEmail = prescription.getAppointment().getPatient().getUser().getEmail();
        if (!patientEmail.equals(requesterEmail))
            throw new BusinessRuleException("Solo el paciente puede usar la receta");
        if (prescription.getUsesRemaining() <= 0)
            throw new BusinessRuleException("La receta ya no tiene usos disponibles");
        if (prescription.getExpirationDate().isBefore(LocalDateTime.now()))
            throw new BusinessRuleException("La receta ha expirado");
        prescription.setUsesRemaining(prescription.getUsesRemaining() - 1);
        return toResponse(prescriptionRepository.save(prescription));
    }

    public List<PrescriptionResponse> findByPatientEmail(String email) {
        return prescriptionRepository.findAll().stream()
            .filter(p -> p.getAppointment().getPatient().getUser().getEmail().equals(email))
            .map(this::toResponse).toList();
    }

    public List<PrescriptionResponse> findByAppointment(Long appointmentId) {
        return prescriptionRepository.findByAppointmentId(appointmentId).stream().map(this::toResponse).toList();
    }

    private String generateHash(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { return UUID.randomUUID().toString(); }
    }

    public PrescriptionResponse toResponse(Prescription p) {
        return PrescriptionResponse.builder().id(p.getId())
            .appointmentId(p.getAppointment().getId()).medicationDetails(p.getMedicationDetails())
            .hashSignature(p.getHashSignature()).usesRemaining(p.getUsesRemaining())
            .expirationDate(p.getExpirationDate()).createdAt(p.getCreatedAt()).build();
    }
}
