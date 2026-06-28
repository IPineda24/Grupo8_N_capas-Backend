package com.uca.telemedicina.service;
import com.uca.telemedicina.dto.request.CreateMedicalNoteRequest;
import com.uca.telemedicina.dto.response.MedicalNoteResponse;
import com.uca.telemedicina.entities.*;
import com.uca.telemedicina.enums.AppointmentStatus;
import com.uca.telemedicina.exception.*;
import com.uca.telemedicina.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class MedicalNoteService {
    private final MedicalNoteRepository noteRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    public MedicalNoteResponse create(CreateMedicalNoteRequest req, String doctorEmail) {
        Appointment appointment = appointmentRepository.findById(req.getAppointmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada: " + req.getAppointmentId()));
        if (!appointment.getDoctor().getUser().getEmail().equals(doctorEmail))
            throw new BusinessRuleException("Solo el médico de la cita puede agregar notas");
        MedicalNote note = MedicalNote.builder().appointment(appointment).notes(req.getNotes()).build();
        return toResponse(noteRepository.save(note));
    }

    public List<MedicalNoteResponse> findByAppointment(Long appointmentId, String requesterEmail) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada: " + appointmentId));
        String patientEmail = appointment.getPatient().getUser().getEmail();
        String doctorEmail = appointment.getDoctor().getUser().getEmail();
        if (!requesterEmail.equals(patientEmail) && !requesterEmail.equals(doctorEmail))
            throw new BusinessRuleException("No tienes acceso al historial de esta cita");
        return noteRepository.findByAppointmentId(appointmentId).stream().map(this::toResponse).toList();
    }

    public MedicalNoteResponse toResponse(MedicalNote n) {
        return MedicalNoteResponse.builder().id(n.getId())
            .appointmentId(n.getAppointment().getId()).notes(n.getNotes())
            .createdAt(n.getCreatedAt()).build();
    }
}
