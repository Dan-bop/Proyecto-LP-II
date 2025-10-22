package com.maysu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maysu.model.Categoria;
import com.maysu.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Listar todas las categorías
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    // Guardar o actualizar una categoría
    public Categoria guardar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    // Buscar categoría por id
    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));
    }

    // Eliminar categoría por id
    public void eliminarPorId(Long id) {
        categoriaRepository.deleteById(id);
    }
}
