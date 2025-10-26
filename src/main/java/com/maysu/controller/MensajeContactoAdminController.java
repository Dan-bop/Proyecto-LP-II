package com.maysu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.maysu.service.MensajeContactoService;

@Controller
@RequestMapping("/admin/mensajes")
@PreAuthorize("hasRole('ADMIN')")
public class MensajeContactoAdminController {

    @Autowired
    private MensajeContactoService mensajeService;

    /**
     * Muestra la lista de todos los mensajes de contacto.
     * URL: /admin/mensajes
     */
    @GetMapping
    public String listarMensajes(Model model) {
        model.addAttribute("mensajes", mensajeService.listarTodos());
        return "admin/mensajes"; // <-- Vista: templates/admin/mensajes.html
    }

    /**
     * Elimina un mensaje.
     * URL: /admin/mensajes/eliminar/{id}
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarMensaje(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            mensajeService.eliminarPorId(id);
            redirectAttrs.addFlashAttribute("exito", "Mensaje eliminado correctamente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al eliminar el mensaje.");
        }
        return "redirect:/admin/mensajes";
    }

    /**
     * Marca un mensaje como "Atendido".
     * URL: /admin/mensajes/marcar-leido/{id}
     */
    @GetMapping("/marcar-leido/{id}")
    public String marcarComoLeido(@PathVariable Long id) {
        mensajeService.marcarComoAtendido(id, true);
        return "redirect:/admin/mensajes";
    }

    /**
     * Marca un mensaje como "No Atendido" (pendiente).
     * URL: /admin/mensajes/marcar-pendiente/{id}
     */
    @GetMapping("/marcar-pendiente/{id}")
    public String marcarComoPendiente(@PathVariable Long id) {
        mensajeService.marcarComoAtendido(id, false);
        return "redirect:/admin/mensajes";
    }
}