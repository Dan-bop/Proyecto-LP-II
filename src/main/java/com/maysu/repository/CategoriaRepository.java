package com.maysu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maysu.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long > {

}
