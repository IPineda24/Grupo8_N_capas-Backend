package com.uca.telemedicina.service;

import com.uca.telemedicina.dto.request.UpdatePatientRequest;
import com.uca.telemedicina.dto.response.PatientResponse;
import com.uca.telemedicina.entities.Patient;
import com.uca.telemedicina.entities.User;
import com.uca.telemedicina.exception.ResourceNotFoundException;
import com.uca.telemedicina.repository.PatientRepository;
import com.uca.telemedicina.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

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
}