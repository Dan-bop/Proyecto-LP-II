package com.maysu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.maysu.model.Producto;
import com.maysu.service.ProductoService;

// Controlador para manejar la vista de productos en oferta
@Controller
public class OfertaController {
	
	    @Autowired
	    private ProductoService productoService;

	    // Ruta que muestra los productos con descuento
	    @GetMapping("/ofertas")
	    public String mostrarOfertas(Model model) {
	        // Obtener productos que tienen un precio de oferta definido
	        List<Producto> productosEnOferta = productoService.listarConDescuento();

	        // Enviar la lista a la vista
	        model.addAttribute("productos", productosEnOferta);
	        return "ofertas"; // Carga la plantilla ofertas.html
	    }
	}

	

