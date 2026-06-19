package com.example.telemedicina.repositories;


import com.example.telemedicina.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Para autenticación o validación de duplicados
    Optional<User> findByEmail(String email);

    // Para buscar solo usuarios que no hayan sufrido borrado lógico
    Long countByEmailAndIsActiveTrue(String email);
}
