package com.maysu.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.maysu.model.ItemCarrito;
import com.maysu.model.Pedido;
import com.maysu.model.Producto;
import com.maysu.model.Usuario;
import com.maysu.service.PedidoService;
import com.maysu.service.ProductoService;
import com.maysu.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CarritoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioService usuarioService;

    @ModelAttribute("carrito")
    public List<ItemCarrito> getCarrito(HttpSession session) {
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        return (carrito != null) ? carrito : new ArrayList<>();
    }

    @PostMapping("/carrito/agregar")
    public String agregarAlCarrito(@RequestParam("productoId") Long id, HttpSession session) {
        Producto producto = productoService.buscarPorId(id);
        if (producto == null) return "redirect:/catalago";

        List<ItemCarrito> carrito = getCarrito(session);
        boolean encontrado = false;

        for (ItemCarrito item : carrito) {
            if (item.getProducto().getId().equals(id)) {
                item.setCantidad(item.getCantidad() + 1);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            carrito.add(new ItemCarrito(producto, 1));
        }

        session.setAttribute("carrito", carrito);
        return "redirect:/catalago";
    }

    @PostMapping("/carrito/eliminar")
    public String eliminarDelCarrito(@RequestParam("productoId") Long id, HttpSession session) {
        List<ItemCarrito> carrito = getCarrito(session);
        carrito.removeIf(item -> item.getProducto().getId().equals(id));
        session.setAttribute("carrito", carrito);
        return "redirect:/carrito";
    }

    @GetMapping("/carrito")
    public String verCarrito(Model model, HttpSession session) {
        List<ItemCarrito> carrito = getCarrito(session);
        double total = carrito.stream().mapToDouble(ItemCarrito::getSubtotal).sum();
        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);
        return "carrito";
    }

    @PostMapping("/pedido/confirmar")
    public String confirmarPedido(HttpSession session, RedirectAttributes redirectAttrs, Principal principal) {
        List<ItemCarrito> carrito = getCarrito(session);
        if (carrito.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "Tu carrito está vacío.");
            return "redirect:/carrito";
        }

        // 🔐 Obtener usuario autenticado
        Usuario cliente = usuarioService.buscarPorEmail(principal.getName());

        // 🧾 Guardar pedido en base de datos
        Pedido pedido = pedidoService.confirmarPedido(carrito, cliente);

        // 🧹 Limpiar carrito
        session.removeAttribute("carrito");

        // ✅ Mensaje de éxito
        redirectAttrs.addFlashAttribute("exito", "Pedido #" + pedido.getId() + " confirmado correctamente.");
        return "redirect:/cuenta/pedidos";
    }
}
