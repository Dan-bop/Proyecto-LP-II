package com.maysu.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName());
        model.addAttribute("usuario", usuario);
        return "editar";
    }
    @PostMapping("/cuenta/editar")
    public String guardarPerfil(@ModelAttribute Usuario usuario, Principal principal) {
        Usuario actual = usuarioService.buscarPorEmail(principal.getName());
        actual.setNombre(usuario.getNombre());
        usuarioService.guardar(actual); // Asegúrate de tener este método en el servicio
        return "redirect:/cuenta";
    }

}
