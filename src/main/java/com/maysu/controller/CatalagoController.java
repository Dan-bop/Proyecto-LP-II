package com.maysu.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.maysu.model.Categoria;
import com.maysu.model.ItemCarrito;
import com.maysu.model.Producto;
import com.maysu.service.CategoriaService;
import com.maysu.service.ProductoService;

import jakarta.servlet.http.HttpSession;


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
	public String mostrarCatalago(@RequestParam(required = false) Long categoriaId, Model model, HttpSession session) {
	    // Obtener categorías y productos (ya implementado)
	    List<Categoria> categorias = categoriaService.listarTodas();
	    List<Producto> productos = (categoriaId != null)
	            ? productosService.listarPorCategoria(categoriaId)
	            : productosService.ListarTodos();

	    // ✅ Asegurar que el carrito exista en sesión
	    if (session.getAttribute("carrito") == null) {
	        session.setAttribute("carrito", new ArrayList<ItemCarrito>());
	    }

	    // ✅ Agregar carrito al modelo para evitar errores en la vista
	    model.addAttribute("carrito", session.getAttribute("carrito"));

	    // Mantener lo que ya funcionaba
	    model.addAttribute("categorias", categorias);
	    model.addAttribute("productos", productos);

	    return "catalago";
	}

	    
	@GetMapping("/detalle/{id}")
    public String mostrarDetalle(@PathVariable Long id, Model model, HttpSession session) {  // ✅ Agrega HttpSession
        Producto producto = productosService.buscarPorId(id);
        model.addAttribute("producto", producto);
        
        // ✅ Corrección: Obtén o inicializa el carrito desde la sesión
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();  // Inicializa como lista vacía si no existe en la sesión
        }
        model.addAttribute("carrito", carrito);  // ✅ Añade carrito al modelo
        
        return "detalle";  // Asegúrate de que coincida con el nombre de tu plantilla (ej. "detalleProducto" si es diferente)
    }

	}

