package com.example.telemedicina.services;

import com.example.telemedicina.dto.LoginRequest;
import com.example.telemedicina.dto.RegisterRequest;
import com.example.telemedicina.entities.User;
import com.example.telemedicina.entities.Role;
import com.example.telemedicina.repositories.UserRepository;
import com.example.telemedicina.repositories.RoleRepository;
import com.example.telemedicina.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }


    public User register(RegisterRequest request) {

        Role defaultRole = roleRepository.findByRoleName("ROLE_PACIENTE")
                .orElseThrow(() -> new RuntimeException("Error del sistema: El rol ROLE_PACIENTE no está configurado en la BD."));

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(defaultRole);
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        return userRepository.save(user);
    }


    public User login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );


        return userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado después de autenticar."));
    }


    public String generateToken(User user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        return jwtUtil.generateToken(userDetails);
    }
}