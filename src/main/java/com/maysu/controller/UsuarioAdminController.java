package com.maysu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.maysu.model.Usuario;
import com.maysu.service.UsuarioService;

@Controller
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioAdminController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Mapeo para listar todos los clientes (ROLE_CLIENTE).
     * URL: /admin/usuarios
     */
    @GetMapping
    public String listarClientes(Model model) {
        // 1. Usamos el NUEVO método del servicio
        List<Usuario> clientes = usuarioService.listarClientes();

        // 2. Los mandamos a la vista
        model.addAttribute("clientes", clientes);

        // 3. Devolvemos el nombre del archivo HTML que vamos a crear
        return "admin/usuarios"; // <-- Vista: templates/admin/usuarios.html
    }

    /**
     * Mapeo para eliminar un cliente (No permitido en requisitos de Roles).
     * URL: /admin/usuarios/eliminar/{id}
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        try {
            // (Opcional) Faltaría lógica para borrar/anonimizar pedidos
            usuarioService.eliminarPorId(id);
            redirectAttrs.addFlashAttribute("exito", "Cliente eliminado correctamente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al eliminar el cliente.");
        }

        return "redirect:/admin/usuarios";
    }
}