package com.maysu.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.maysu.model.Rol;
import com.maysu.model.Usuario;
import com.maysu.repository.RolRepository;
import com.maysu.repository.UsuarioRepository;
import com.maysu.service.UsuarioService;

@Controller
@RequestMapping("/admin/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RolAdminController {

    @Autowired
    private UsuarioService usuarioService;

    // Necesitamos acceso directo a estos para crear usuarios
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Mapeo para listar los usuarios internos (ADMIN, EMPLEADO).
     * URL: /admin/roles
     */
    @GetMapping
    public String listarUsuariosInternos(Model model) {
        model.addAttribute("usuariosInternos", usuarioService.listarUsuariosInternos());
        return "admin/roles"; // <-- Vista: templates/admin/roles.html
    }

    /**
     * Muestra el formulario para crear un nuevo usuario interno.
     * URL: /admin/roles/nuevo
     */
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        // Buscamos los roles internos para el <select>
        List<Rol> roles = rolRepository.findByNombreIn(List.of("ROLE_ADMIN", "ROLE_EMPLEADO"));

        model.addAttribute("usuario", new Usuario());
        model.addAttribute("rolesDisponibles", roles);

        return "admin/form-rol"; // <-- Vista: templates/admin/form-rol.html
    }

    /**
     * Guarda el nuevo usuario interno.
     * URL: /admin/roles/guardar
     */
    @PostMapping("/guardar")
    public String guardarUsuarioInterno(@ModelAttribute Usuario usuario,
                                        @RequestParam Long rolId, // Recibimos el ID del rol desde el <select>
                                        RedirectAttributes redirectAttrs) {

        // Verificamos si el email ya existe
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            redirectAttrs.addFlashAttribute("error", "El email ya está registrado.");
            return "redirect:/admin/roles/nuevo";
        }

        // 1. Encriptamos la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // 2. Buscamos y asignamos el rol
        Rol rol = rolRepository.findById(rolId).orElse(null);
        if (rol == null) {
            redirectAttrs.addFlashAttribute("error", "Rol no válido.");
            return "redirect:/admin/roles/nuevo";
        }
        usuario.setRoles(Set.of(rol));

        // 3. Guardamos el nuevo usuario
        usuarioRepository.save(usuario);

        redirectAttrs.addFlashAttribute("exito", "Usuario interno creado correctamente.");
        return "redirect:/admin/roles";
    }
}