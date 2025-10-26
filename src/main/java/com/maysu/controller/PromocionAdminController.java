package com.maysu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.maysu.model.Promocion;
import com.maysu.service.PromocionesService;

@Controller
@RequestMapping("/admin/promociones")
@PreAuthorize("hasRole('ADMIN')")
public class PromocionAdminController {

    @Autowired
    private PromocionesService promocionesService;

    /**
     * Muestra la lista de todas las promociones.
     * URL: /admin/promociones
     */
    @GetMapping
    public String listarPromociones(Model model) {
        model.addAttribute("promociones", promocionesService.listarTodas());
        return "admin/promociones"; // <-- Vista: templates/admin/promociones.html
    }

    /**
     * Muestra el formulario para crear una nueva promoción.
     * URL: /admin/promociones/nuevo
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("promocion", new Promocion());
        return "admin/form-promocion"; // <-- Vista: templates/admin/form-promocion.html
    }

    /**
     * Muestra el formulario para editar una promoción existente.
     * URL: /admin/promociones/editar/{id}
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Promocion promocion = promocionesService.buscarPorId(id);
        model.addAttribute("promocion", promocion);
        return "admin/form-promocion";
    }

    /**
     * Guarda una promoción (nueva o editada).
     * URL: /admin/promociones/guardar
     */
    @PostMapping("/guardar")
    public String guardarPromocion(@ModelAttribute Promocion promocion, RedirectAttributes redirectAttrs) {
        // El servicio 'guardar' ya existía
        promocionesService.guardar(promocion);
        redirectAttrs.addFlashAttribute("exito", "Promoción guardada correctamente.");
        return "redirect:/admin/promociones";
    }

    /**
     * Elimina una promoción.
     * URL: /admin/promociones/eliminar/{id}
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarPromocion(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            // Usamos el nuevo método del servicio
            promocionesService.eliminarPorId(id);
            redirectAttrs.addFlashAttribute("exito", "Promoción eliminada permanentemente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al eliminar la promoción.");
        }
        return "redirect:/admin/promociones";
    }
}