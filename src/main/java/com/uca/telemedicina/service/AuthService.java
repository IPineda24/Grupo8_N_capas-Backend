package com.uca.telemedicina.service;
import com.uca.telemedicina.dto.request.*;
import com.uca.telemedicina.dto.response.*;
import com.uca.telemedicina.entities.*;
import com.uca.telemedicina.exception.*;
import com.uca.telemedicina.repository.*;
import com.uca.telemedicina.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    public JwtAuthResponse login(LoginRequest request) {
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String token = jwtUtil.generateToken(auth);
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return JwtAuthResponse.builder().accessToken(token)
            .user(UserResponse.builder().id(user.getId()).firstName(user.getFirstName())
                .lastName(user.getLastName()).email(user.getEmail()).phone(user.getPhone())
                .role(user.getRole().getRoleName()).isActive(user.getIsActive()).build()).build();
    }
    @Transactional
    public JwtAuthResponse register(RegisterPatientRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new BusinessRuleException("El email ya está registrado: " + request.getEmail());
        Role role = roleRepository.findByRoleName("ROLE_PATIENT")
            .orElseThrow(() -> new ResourceNotFoundException("Rol ROLE_PATIENT no encontrado"));
        User user = User.builder().role(role).firstName(request.getFirstName())
            .lastName(request.getLastName()).email(request.getEmail()).phone(request.getPhone())
            .password(passwordEncoder.encode(request.getPassword())).isActive(true).build();
        userRepository.save(user);
        Patient patient = Patient.builder().user(user).dateOfBirth(request.getDateOfBirth())
            .weightLbs(request.getWeightLbs()).heightCm(request.getHeightCm()).isActive(true).build();
        patientRepository.save(patient);
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        String token = jwtUtil.generateToken(auth);
        return JwtAuthResponse.builder().accessToken(token)
            .user(UserResponse.builder().id(user.getId()).firstName(user.getFirstName())
                .lastName(user.getLastName()).email(user.getEmail()).phone(user.getPhone())
                .role(role.getRoleName()).isActive(true).build()).build();
    }
}
