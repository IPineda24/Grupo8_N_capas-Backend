package com.example.telemedicina.config;

import com.example.telemedicina.entities.Role;
import com.example.telemedicina.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Insertar roles con el prefijo ROLE_ requerido por Spring Security
        crearRolSiNoExiste("ROLE_ADMIN");
        crearRolSiNoExiste("ROLE_DOCTOR");
        crearRolSiNoExiste("ROLE_PACIENTE");
    }

    private void crearRolSiNoExiste(String nombreRol) {
        if (roleRepository.findByRoleName(nombreRol).isEmpty()) {
            Role rol = new Role();
            rol.setRoleName(nombreRol);
            roleRepository.save(rol);
            System.out.println("Rol inicial creado con UUID: " + nombreRol);
        }
    }
}