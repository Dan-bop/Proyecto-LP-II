package com.maysu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maysu.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{
	Optional<Producto> findByNombre(String nombre); //Busca un solo producto
	List<Producto> findAllByNombreContainingIgnoreCase(String nombre); // Busca varios con coincidencias Parciales

}

