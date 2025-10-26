package com.maysu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maysu.model.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
	Optional<Rol> findByNombre(String nombre);
	
	/**
     * Busca roles por una lista de nombres.
     */
    List<Rol> findByNombreIn(List<String> nombres);
}
