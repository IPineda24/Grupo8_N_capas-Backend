package com.uca.telemedicina.service;

import com.uca.telemedicina.dto.response.AttachmentResponse;
import com.uca.telemedicina.entities.*;
import com.uca.telemedicina.exception.*;
import com.uca.telemedicina.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Transactional
    public AttachmentResponse upload(Long appointmentId, String uploaderEmail,
                                     String fileName, String fileUrl, String documentType) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada: " + appointmentId));

        String patientEmail = appointment.getPatient().getUser().getEmail();
        String doctorEmail  = appointment.getDoctor().getUser().getEmail();
        if (!uploaderEmail.equals(patientEmail) && !uploaderEmail.equals(doctorEmail))
            throw new BusinessRuleException("No tienes permiso para adjuntar documentos en esta cita");

        User uploader = userRepository.findByEmail(uploaderEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Attachment attachment = Attachment.builder()
                .appointment(appointment)
                .uploadedBy(uploader)
                .fileName(fileName)
                .fileUrl(fileUrl)
                .documentType(documentType)
                .build();

        return toResponse(attachmentRepository.save(attachment));
    }

    public List<AttachmentResponse> findByAppointment(Long appointmentId, String requesterEmail) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada: " + appointmentId));

        String patientEmail = appointment.getPatient().getUser().getEmail();
        String doctorEmail  = appointment.getDoctor().getUser().getEmail();
        if (!requesterEmail.equals(patientEmail) && !requesterEmail.equals(doctorEmail))
            throw new BusinessRuleException("No tienes acceso a los adjuntos de esta cita");

        return attachmentRepository.findByAppointmentId(appointmentId)
                .stream().map(this::toResponse).toList();
    }

    public AttachmentResponse toResponse(Attachment a) {
        return AttachmentResponse.builder()
                .id(a.getId())
                .appointmentId(a.getAppointment().getId())
                .uploadedBy(a.getUploadedBy().getFirstName() + " " + a.getUploadedBy().getLastName())
                .documentType(a.getDocumentType())
                .fileName(a.getFileName())
                .fileUrl(a.getFileUrl())
                .createdAt(a.getCreatedAt())
                .build();
    }
}