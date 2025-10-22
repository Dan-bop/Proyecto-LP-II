package com.maysu.controller;

import com.maysu.model.Categoria;
import com.maysu.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categorias")
@PreAuthorize("hasRole('ADMIN')")
public class CategoriaAdminController {

    @Autowired
    private CategoriaService categoriaService;

    // Listar categorías
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "admin/categorias";
    }

    // Mostrar formulario de nueva categoría
    @GetMapping("/nuevo")
    public String nueva(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "admin/formCategoria";
    }

    // Guardar categoría
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Categoria categoria, RedirectAttributes redirectAttrs) {
        categoriaService.guardar(categoria);
        redirectAttrs.addFlashAttribute("exito", "Categoría guardada correctamente.");
        return "redirect:/admin/categorias";
    }

    // Editar categoría
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Categoria categoria = categoriaService.buscarPorId(id);
        model.addAttribute("categoria", categoria);
        return "admin/formCategoria";
    }

    // Eliminar categoría
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            categoriaService.eliminarPorId(id);
            redirectAttrs.addFlashAttribute("exito", "Categoría eliminada correctamente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "No se puede eliminar: tiene productos asociados.");
        }
        return "redirect:/admin/categorias";
    }
}
