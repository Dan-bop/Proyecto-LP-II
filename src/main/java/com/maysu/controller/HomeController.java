package com.maysu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.maysu.model.Producto;
import com.maysu.model.Promocion;
import com.maysu.service.ProductoService;
import com.maysu.service.PromocionesService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class HomeController {
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private PromocionesService promocionService;
	
	@GetMapping("/")
	public String mostrarHome(Model model) {
		List<Producto> productos =productoService.ListarTodos();
		List<Promocion> promociones = promocionService.obtenerActivas();
		
		model.addAttribute("productos",productos);
		model.addAttribute("promociones", promociones);
		return "index";
	}
	

}
