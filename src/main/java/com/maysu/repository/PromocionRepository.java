package com.maysu.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maysu.model.Promocion;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Long> {
	List<Promocion> findByActivaTrueAndFechaInicioBeforeAndFechaFinAfter(LocalDate hoy1, LocalDate hoy2);

}
