package com.maysu.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.maysu.model.*;
import com.maysu.service.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class CarritoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioService usuarioService;

    // 🔄 Carga el carrito desde la sesión para que esté disponible en todas las vistas
    @ModelAttribute("carrito")
    public List<ItemCarrito> getCarrito(HttpSession session) {
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        return (carrito != null) ? carrito : new ArrayList<>();
    }

    // 🛒 Agrega un producto al carrito con validación de stock y estado activo
    @PostMapping("/carrito/agregar")
    public String agregarAlCarrito(@RequestParam Long productoId,
                                   @RequestParam(defaultValue = "1") int cantidad,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Producto producto = productoService.buscarPorId(productoId);

        // ❌ Validación: producto inexistente, inactivo o sin stock suficiente
        if (producto == null || !producto.isActivo() || producto.getStock() < cantidad) {
            redirectAttributes.addFlashAttribute("error", "Producto no disponible o sin stock suficiente.");
            return "redirect:/catalago";
        }

        List<ItemCarrito> carrito = getCarrito(session);
        boolean encontrado = false;

        // 🔁 Si el producto ya está en el carrito, actualiza la cantidad
        for (ItemCarrito item : carrito) {
            if (item.getProducto().getId().equals(productoId)) {
                int nuevaCantidad = item.getCantidad() + cantidad;

                // ❌ Validación: no permitir superar el stock disponible
                if (nuevaCantidad > producto.getStock()) {
                    redirectAttributes.addFlashAttribute("error", "No hay suficiente stock para agregar más unidades.");
                    return "redirect:/catalago";
                }

                item.setCantidad(nuevaCantidad);
                encontrado = true;
                break;
            }
        }

        // ➕ Si no está en el carrito, lo agrega como nuevo ítem
        if (!encontrado) {
            carrito.add(new ItemCarrito(producto, cantidad));
        }

        session.setAttribute("carrito", carrito);
        redirectAttributes.addFlashAttribute("exito", "Producto agregado al carrito.");
        return "redirect:/catalago";
    }

    // 🗑️ Elimina un producto del carrito
    @PostMapping("/carrito/eliminar")
    public String eliminarDelCarrito(@RequestParam("productoId") Long id, HttpSession session) {
        List<ItemCarrito> carrito = getCarrito(session);
        carrito.removeIf(item -> item.getProducto().getId().equals(id));
        session.setAttribute("carrito", carrito);
        return "redirect:/carrito";
    }

    // 👀 Muestra el contenido del carrito
    @GetMapping("/carrito")
    public String verCarrito(Model model, HttpSession session) {
        List<ItemCarrito> carrito = getCarrito(session);
        double total = carrito.stream().mapToDouble(ItemCarrito::getSubtotal).sum();
        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);
        return "carrito";
    }

    // ✅ Confirma el pedido y guarda los detalles
    @PostMapping("/pedido/confirmar")
    public String confirmarPedido(@RequestParam String direccion,
                                  @RequestParam String telefono,
                                  @RequestParam String metodoPago,
                                  HttpSession session,
                                  RedirectAttributes redirectAttrs) {
        // 👤 Obtiene el usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario cliente = (Usuario) auth.getPrincipal();

        List<ItemCarrito> carrito = getCarrito(session);
        if (carrito.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "Tu carrito está vacío.");
            return "redirect:/carrito";
        }

        // 🧾 Crea el pedido base
        Pedido pedido = new Pedido();
        pedido.setUsuario(cliente);
        pedido.setFecha(LocalDate.now());
        pedido.setEstado("Pendiente");
        pedido.setDireccion(direccion);
        pedido.setTelefono(telefono);
        pedido.setMetodoPago(metodoPago);

        List<DetallePedido> detalles = new ArrayList<>();
        double total = 0;

        // 🔁 Recorre los ítems del carrito y crea los detalles del pedido
        for (ItemCarrito item : carrito) {
            Producto producto = item.getProducto();

            // ❌ Validación: producto inactivo o sin stock suficiente
            if (!producto.isActivo() || producto.getStock() < item.getCantidad()) {
                redirectAttrs.addFlashAttribute("error", "El producto '" + producto.getNombre() + "' ya no está disponible o no hay suficiente stock.");
                return "redirect:/carrito";
            }

            // 🧩 Crea el detalle del pedido
            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio() * item.getCantidad());
            detalle.setPedido(pedido);
            detalles.add(detalle);
            total += detalle.getSubtotal();

            // 📉 Descuenta el stock del producto
            producto.setStock(producto.getStock() - item.getCantidad());
            productoService.guardar(producto);
        }

        pedido.setDetalles(detalles);
        pedido.setTotal(total);

        // 💾 Guarda el pedido completo
        pedidoService.guardar(pedido);

        // 🧹 Limpia el carrito
        session.setAttribute("carrito", new ArrayList<>());

        redirectAttrs.addFlashAttribute("exito", "Pedido #" + pedido.getId() + " confirmado correctamente.");
        return "redirect:/pedido/confirmado";
    }

    // ✅ Muestra la confirmación del último pedido
    @GetMapping("/pedido/confirmado")
    public String mostrarConfirmacion(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario cliente = (Usuario) auth.getPrincipal();
        Pedido pedido = pedidoService.buscarUltimoPedidoDe(cliente);

        String fechaFormateada = pedido.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        model.addAttribute("pedido", pedido);
        model.addAttribute("fechaFormateada", fechaFormateada);

        return "pedidoConfirmado";
    }

    // ✅ Muestra la vista de checkout antes de confirmar
    @GetMapping("/checkout")
    public String mostrarCheckout(HttpSession session, RedirectAttributes redirectAttrs, Model model) {
        List<ItemCarrito> carrito = getCarrito(session);
        if (carrito.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "Tu carrito está vacío.");
            return "redirect:/carrito";
        }
        model.addAttribute("carrito", carrito);
        return "checkout";
    }
}
