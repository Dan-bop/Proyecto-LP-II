package com.maysu.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maysu.model.Promocion;
import com.maysu.repository.PromocionRepository;

@Service
public class PromocionesService {
	
	@Autowired
	private PromocionRepository promocionRepository;
	
	public List<Promocion> obtenerActivas() {
		LocalDate hoy = LocalDate.now();
		return promocionRepository
				.findByActivaTrueAndFechaInicioBeforeAndFechaFinAfter(hoy, hoy);
	}
	
	public Promocion guardar (Promocion promocion) {
		return promocionRepository.save(promocion);
	}
	
	public void inactivar (Long id) {
		Promocion promo =promocionRepository.findById(id).orElse(null);
		if (promo !=null) {
			promo.setActiva(false);
			promocionRepository.save(promo);
		}
	}

}
