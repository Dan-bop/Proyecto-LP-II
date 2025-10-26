package com.maysu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maysu.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Busca todos los usuarios que tengan un rol con un nombre específico.
     * (Ej. "ROLE_CLIENTE")
     */
    List<Usuario> findByRoles_Nombre(String nombreRol);
    
    /**
     * Busca usuarios que tengan CUALQUIERA de los roles en la lista.
     */
    List<Usuario> findByRoles_NombreIn(List<String> roles);
}


