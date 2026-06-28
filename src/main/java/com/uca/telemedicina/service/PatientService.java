package com.uca.telemedicina.service;

import com.uca.telemedicina.dto.request.UpdatePatientRequest;
import com.uca.telemedicina.dto.response.*;
import com.uca.telemedicina.entities.Patient;
import com.uca.telemedicina.entities.User;
import com.uca.telemedicina.exception.BusinessRuleException;
import com.uca.telemedicina.exception.ResourceNotFoundException;
import com.uca.telemedicina.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicalNoteRepository medicalNoteRepository;

    public List<PatientResponse> findAll() {
        return patientRepository.findAll().stream().map(this::toResponse).toList();
    }

    public PatientResponse findById(Long id) {
        return toResponse(patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado: " + id)));
    }

    public PatientResponse findByUserId(Long userId) {
        return toResponse(patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado para userId: " + userId)));
    }

    public PatientResponse findByEmail(String email) {
        return patientRepository.findAll().stream()
                .filter(p -> p.getUser().getEmail().equals(email))
                .findFirst()
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado para email: " + email));
    }

    @Transactional
    public PatientResponse update(String email, UpdatePatientRequest req) {
        Patient patient = patientRepository.findAll().stream()
                .filter(p -> p.getUser().getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));
        User user = patient.getUser();
        if (req.getFirstName() != null) user.setFirstName(req.getFirstName());
        if (req.getLastName()  != null) user.setLastName(req.getLastName());
        if (req.getPhone()     != null) user.setPhone(req.getPhone());
        if (req.getWeightLbs() != null) patient.setWeightLbs(req.getWeightLbs());
        if (req.getHeightCm()  != null) patient.setHeightCm(req.getHeightCm());
        userRepository.save(user);
        return toResponse(patientRepository.save(patient));
    }

    public PatientResponse toResponse(Patient p) {
        return PatientResponse.builder()
                .id(p.getId())
                .firstName(p.getUser().getFirstName())
                .lastName(p.getUser().getLastName())
                .email(p.getUser().getEmail())
                .phone(p.getUser().getPhone())
                .dateOfBirth(p.getDateOfBirth())
                .weightLbs(p.getWeightLbs())
                .heightCm(p.getHeightCm())
                .isActive(p.getIsActive())
                .build();
    }

    public PatientHistoryResponse getHistory(Long patientId, String requesterEmail) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado: " + patientId));

        // Solo el propio paciente, un doctor o admin pueden ver el historial
        boolean isPatient = patient.getUser().getEmail().equals(requesterEmail);
        boolean isDoctor  = requesterEmail != null && !isPatient;
        if (!isPatient && !isDoctor)
            throw new BusinessRuleException("No tienes acceso al historial de este paciente");

        List<com.uca.telemedicina.entities.Appointment> appointments =
                appointmentRepository.findByPatientId(patientId);

        List<PatientHistoryResponse.AppointmentHistoryEntry> entries = appointments.stream()
                .map(a -> {
                    List<PrescriptionResponse> prescriptions =
                            prescriptionRepository.findByAppointmentId(a.getId())
                                    .stream().map(p -> PrescriptionResponse.builder()
                                            .id(p.getId())
                                            .appointmentId(a.getId())
                                            .medicationDetails(p.getMedicationDetails())
                                            .hashSignature(p.getHashSignature())
                                            .usesRemaining(p.getUsesRemaining())
                                            .expirationDate(p.getExpirationDate())
                                            .createdAt(p.getCreatedAt())
                                            .build()).toList();

                    List<MedicalNoteResponse> notes =
                            medicalNoteRepository.findByAppointmentId(a.getId())
                                    .stream().map(n -> MedicalNoteResponse.builder()
                                            .id(n.getId())
                                            .appointmentId(a.getId())
                                            .notes(n.getNotes())
                                            .createdAt(n.getCreatedAt())
                                            .build()).toList();

                    return PatientHistoryResponse.AppointmentHistoryEntry.builder()
                            .appointment(AppointmentResponse.builder()
                                    .id(a.getId())
                                    .appointmentDate(a.getAppointmentDate())
                                    .status(a.getStatus())
                                    .meetingLink(a.getMeetingLink())
                                    .rating(a.getRating())
                                    .createdAt(a.getCreatedAt())
                                    .build())
                            .prescriptions(prescriptions)
                            .medicalNotes(notes)
                            .build();
                }).toList();

        long completed  = appointments.stream()
                .filter(a -> a.getStatus().name().equals("COMPLETED")).count();
        long cancelled  = appointments.stream()
                .filter(a -> a.getStatus().name().equals("CANCELLED")).count();

        return PatientHistoryResponse.builder()
                .patient(toResponse(patient))
                .appointments(entries)
                .totalAppointments(appointments.size())
                .completedAppointments((int) completed)
                .cancelledAppointments((int) cancelled)
                .build();
    }
}