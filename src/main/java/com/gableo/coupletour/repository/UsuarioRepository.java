package com.gableo.coupletour.repository;

import com.gableo.coupletour.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    boolean existsByUniqueToken(String uniqueToken);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByPublicId(String publicId);
    Optional<Usuario> findByUniqueToken(String uniqueToken);
}
