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
	/**
     * (Admin) Lista TODAS las promociones, activas e inactivas.
     */
    public List<Promocion> listarTodas() {
        return promocionRepository.findAll();
    }

    /**
     * (Admin) Busca una promoción por su ID para el formulario de editar.
     */
    public Promocion buscarPorId(Long id) {
        return promocionRepository.findById(id).orElse(null);
    }

    /**
     * (Admin) Elimina una promoción de la base de datos.
     */
    public void eliminarPorId(Long id) {
        promocionRepository.deleteById(id);
    }

}
