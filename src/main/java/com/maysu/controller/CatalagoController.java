package com.maysu.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.maysu.model.Categoria;
import com.maysu.model.ItemCarrito;
import com.maysu.model.Producto;
import com.maysu.service.CategoriaService;
import com.maysu.service.ProductoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CatalagoController {

    @Autowired
    private ProductoService productosService;

    @Autowired
    private CategoriaService categoriaService;

    // 🟢 Muestra el catálogo general o filtrado por categoría
    @GetMapping("/catalago")
    public String mostrarCatalago(@RequestParam(required = false) Long categoriaId,
                                  Model model,
                                  HttpSession session) {
        List<Categoria> categorias = categoriaService.listarTodas();

        // ✅ Filtra productos activos y con stock
        List<Producto> productos = (categoriaId != null)
                ? productosService.listarDisponiblesPorCategoria(categoriaId)
                : productosService.listarDisponibles();

        // ✅ Asegura que el carrito exista en sesión
        if (session.getAttribute("carrito") == null) {
            session.setAttribute("carrito", new ArrayList<ItemCarrito>());
        }

        // ✅ Agrega datos al modelo
        model.addAttribute("categorias", categorias);
        model.addAttribute("productos", productos);
        model.addAttribute("carrito", session.getAttribute("carrito"));

        return "catalago";
    }

    // 🔍 Muestra resultados de búsqueda reutilizando la vista del catálogo
    @GetMapping("/buscar")
    public String buscarProductos(@RequestParam("query") String query,
                                  Model model,
                                  HttpSession session) {
        List<Producto> productos = productosService.buscarPorNombreParcial(query);
        List<Categoria> categorias = categoriaService.listarTodas();

        // ✅ Asegura que el carrito exista en sesión
        if (session.getAttribute("carrito") == null) {
            session.setAttribute("carrito", new ArrayList<ItemCarrito>());
        }

        // ✅ Agrega datos al modelo
        model.addAttribute("categorias", categorias);
        model.addAttribute("productos", productos);
        model.addAttribute("carrito", session.getAttribute("carrito"));
        model.addAttribute("busqueda", query); // Para mostrar mensaje si no hay resultados

        return "catalago";
    }

    // 🔍 Muestra el detalle de un producto
    @GetMapping("/detalle/{id}")
    public String mostrarDetalle(@PathVariable Long id,
                                 Model model,
                                 HttpSession session) {
        Producto producto = productosService.buscarPorId(id);

        // ✅ Asegura que el carrito exista en sesión
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        // ✅ Agrega datos al modelo
        model.addAttribute("producto", producto);
        model.addAttribute("carrito", carrito);

        return "detalle"; // Asegúrate de tener esta vista en templates/
    }
}