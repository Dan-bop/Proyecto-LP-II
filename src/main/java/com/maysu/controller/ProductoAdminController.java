package com.maysu.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.maysu.model.Producto;
import com.maysu.service.CategoriaService;
import com.maysu.service.ProductoService;

@Controller
@RequestMapping("/admin/productos")
@PreAuthorize("hasRole('ADMIN')")
public class ProductoAdminController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    // 🔍 Buscador por nombre
    @GetMapping("/buscar")
    public String buscarProductos(@RequestParam("query") String query, Model model) {
        List<Producto> productos = productoService.buscarPorNombreParcial(query);
        model.addAttribute("productos", productos);
        return "admin/productos";
    }

    // 🟢 Mostrar formulario de creación
    @GetMapping("/nuevo")
    public String nuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "admin/formProducto";
    }

    // 💾 Guardar producto (nuevo o editado)
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto,
                                  @RequestParam("imagen") MultipartFile archivo,
                                  RedirectAttributes redirectAttrs) {
        // 🖼️ Procesar imagen si se subió
        if (!archivo.isEmpty()) {
            try {
                String nombreLimpio = producto.getNombre().toLowerCase().replaceAll("\\s+", "-");
                String nombreArchivo = nombreLimpio + "-" + System.currentTimeMillis() + ".jpg";

                Path ruta = Paths.get("D:/imagenes", nombreArchivo);
                Files.write(ruta, archivo.getBytes());

                producto.setImagenUrl("/images/productos/" + nombreArchivo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // ⚙️ Automatizar estado según stock
        producto.setActivo(producto.getStock() > 0);

        productoService.guardar(producto);
        redirectAttrs.addFlashAttribute("exito", "Producto guardado correctamente.");
        return "redirect:/admin/productos";
    }

    // 📋 Listar todos los productos
    @GetMapping
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.ListarTodos());
        return "admin/productos";
    }

    // ✏️ Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        Producto producto = productoService.buscarPorId(id);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "admin/formProducto";
    }

    // 🗑️ Eliminar producto
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        productoService.eliminarPorId(id);
        redirectAttrs.addFlashAttribute("exito", "Producto eliminado correctamente.");
        return "redirect:/admin/productos";
    }
}