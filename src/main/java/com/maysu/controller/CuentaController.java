package com.maysu.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.maysu.model.Pedido;
import com.maysu.model.Usuario;
import com.maysu.service.UsuarioService;
import com.maysu.service.PedidoService;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public class CuentaController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/cuenta")
    public String mostrarCuenta(Model model, Principal principal) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        model.addAttribute("usuario", usuario);
        return "cuenta";
    }

    @GetMapping("/cuenta/pedidos")
    public String verPedidos(Model model, Principal principal) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        List<Pedido> pedidos = pedidoService.listarPorUsuario(usuario.getId());
        model.addAttribute("pedidos", pedidos);
        return "pedidos";
    }

    @GetMapping("/cuenta/editar")
    public String editarPerfil(Model model, Principal principal) {
        // ✅ Obtener el usuario autenticado por correo
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        model.addAttribute("usuario", usuario);
        return "editar"; // Carga editar.html
    }

    @PostMapping("/cuenta/editar")
    public String guardarPerfil(@ModelAttribute Usuario usuario,
                                Principal principal,
                                RedirectAttributes redirectAttrs) {
        // ✅ Obtener el usuario actual desde la base de datos
        Usuario actual = usuarioService.buscarPorEmail(principal.getName());

        // ✅ Actualizar los campos editables
        actual.setNombre(usuario.getNombre());
        actual.setApellido(usuario.getApellido());
        actual.setEmail(usuario.getEmail());

        // ✅ Si se envió una nueva contraseña, encriptarla
        if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
            actual.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        // ✅ Guardar en la base de datos
        usuarioService.guardar(actual);

        // ✅ Actualizar el objeto autenticado en sesión
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(actual, actual.getPassword(), actual.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // ✅ Mensaje de éxito
        redirectAttrs.addFlashAttribute("exito", "Perfil actualizado correctamente.");

        // ✅ Redirigir a la vista cuenta.html
        return "redirect:/cuenta";
    }
}
