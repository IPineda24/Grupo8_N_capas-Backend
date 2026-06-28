package com.uca.telemedicina.service;
import com.uca.telemedicina.dto.request.*;
import com.uca.telemedicina.dto.response.SpecialtyResponse;
import com.uca.telemedicina.entities.Specialty;
import com.uca.telemedicina.exception.*;
import com.uca.telemedicina.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class SpecialtyService {
    private final SpecialtyRepository specialtyRepository;
    public List<SpecialtyResponse> findAll() {
        return specialtyRepository.findByIsActiveTrue().stream().map(this::toResponse).toList();
    }
    public SpecialtyResponse findById(Long id) {
        return toResponse(specialtyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada: " + id)));
    }
    @Transactional
    public SpecialtyResponse create(CreateSpecialtyRequest req) {
        Specialty s = Specialty.builder().name(req.getName()).description(req.getDescription())
            .minAge(req.getMinAge()).isActive(true).build();
        return toResponse(specialtyRepository.save(s));
    }
    @Transactional
    public SpecialtyResponse update(Long id, UpdateSpecialtyRequest req) {
        Specialty s = specialtyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada: " + id));
        if (req.getName() != null) s.setName(req.getName());
        if (req.getDescription() != null) s.setDescription(req.getDescription());
        if (req.getMinAge() != null) s.setMinAge(req.getMinAge());
        if (req.getIsActive() != null) s.setIsActive(req.getIsActive());
        return toResponse(specialtyRepository.save(s));
    }
    @Transactional
    public void deactivate(Long id) {
        Specialty s = specialtyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada: " + id));
        s.setIsActive(false);
        specialtyRepository.save(s);
    }
    public SpecialtyResponse toResponse(Specialty s) {
        return SpecialtyResponse.builder().id(s.getId()).name(s.getName())
            .description(s.getDescription()).minAge(s.getMinAge()).isActive(s.getIsActive()).build();
    }
}
