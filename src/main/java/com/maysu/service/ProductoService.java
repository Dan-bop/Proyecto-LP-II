package com.maysu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maysu.model.Producto;
import com.maysu.repository.ProductoRepository;

@Service
public class ProductoService {
	
	@Autowired
	private ProductoRepository productoRepository;
	
	public List<Producto> ListarTodos() {
		return productoRepository.findAll();
	}
	
	public Producto guardar(Producto producto) {
		return productoRepository.save(producto);
		
	}
	
	public Producto buscarPorId(Long id) {
		return productoRepository.findById(id).orElse(null);
		
	}
	public Producto buscarPorNombre(String nombre) {
	    return productoRepository.findByNombre(nombre).orElse(null);
	}

	public List<Producto> buscarPorNombreParcial(String nombre) {
	    return productoRepository.findAllByNombreContainingIgnoreCase(nombre);
	}

	
	public void eliminarPorId(Long id) {
		productoRepository.deleteById(id);
	}
}

