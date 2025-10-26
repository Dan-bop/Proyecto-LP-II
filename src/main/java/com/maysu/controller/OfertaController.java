package com.maysu.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

import com.maysu.model.Producto;
import com.maysu.model.ItemCarrito;
import com.maysu.service.ProductoService;

// ✅ Controlador para manejar la vista de productos en oferta
@Controller
public class OfertaController {

    // ✅ Inyección del servicio que gestiona los productos
    @Autowired
    private ProductoService productoService;

    // ✅ Ruta que muestra los productos con descuento
    @GetMapping("/ofertas")
    public String mostrarOfertas(Model model, HttpSession session) {
        // ✅ Obtener productos que tienen un precio de oferta definido
        List<Producto> productosEnOferta = productoService.listarConDescuento();

        // ✅ Asegurar que el carrito exista en sesión para evitar errores en la vista
        if (session.getAttribute("carrito") == null) {
            session.setAttribute("carrito", new ArrayList<ItemCarrito>());
        }

        // ✅ Agregar el carrito al modelo para que pueda usarse en la vista sin romperse
        model.addAttribute("carrito", session.getAttribute("carrito"));

        // ✅ Enviar la lista de productos en oferta a la vista
        model.addAttribute("productos", productosEnOferta);

        // ✅ Cargar la plantilla ofertas.html
        return "ofertas";
    }
}
