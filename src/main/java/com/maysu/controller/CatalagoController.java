package com.maysu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.maysu.model.Categoria;
import com.maysu.model.Producto;
import com.maysu.service.CategoriaService;
import com.maysu.service.ProductoService;


//Controlador para manejar la vista del catálogo público
@Controller
public class CatalagoController {
	
	// Inyección del servicio de productos
	@Autowired
	private ProductoService productosService;
	
	// Inyección del servicio de categorías
	@Autowired
	private CategoriaService categoriaService;
	
	// Método que responde a la ruta /catalogo
	@GetMapping("/catalago")
	public String mostrarCatalago (@RequestParam(required = false) Long categoriaId, Model model) {
		 // Obtener todas las categorías para mostrar en el filtro lateral
		List<Categoria> categorias = categoriaService.listarTodas();
		// Obtener productos según si se filtró por categoría o no
		List<Producto> productos = (categoriaId != null)
				? productosService.listarPorCategoria(categoriaId)
				: productosService.ListarTodos();
		
		model.addAttribute("categorias", categorias);
		model.addAttribute("productos", productos);
		return "catalago";
	}

}
