package com.maysu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;

import com.maysu.model.Pedido;
import com.maysu.service.PedidoService;

@Controller
@RequestMapping("/admin/pedidos")
@PreAuthorize("hasRole('ADMIN')")
public class PedidoAdminController {

    @Autowired
    private PedidoService pedidoService;

    /**
     * Mapeo para listar todos los pedidos, con filtro opcional por estado.
     * URL: /admin/pedidos
     * URL con filtro: /admin/pedidos?estado=Pendiente
     */
    @GetMapping
    public String listarPedidos(@RequestParam(required = false) String estado, Model model) {

        List<Pedido> pedidos;

        // 1. Verificamos si el parámetro "estado" vino en la URL
        if (estado != null && !estado.isEmpty()) {
            // Si hay filtro, usamos el nuevo método del servicio
            pedidos = pedidoService.listarPorEstado(estado);
        } else {
            // Si no hay filtro, mostramos todos
            pedidos = pedidoService.listarTodos();
        }

        // 2. Pasamos la lista (filtrada o completa) a la vista
        model.addAttribute("pedidos", pedidos);

        // 3. Pasamos el estado actual de vuelta a la vista (para el dropdown)
        model.addAttribute("estadoActual", estado); 

        return "admin/pedidos"; 
    }
    
    /**
     * Mapeo para ver el detalle de un pedido específico.
     * URL: /admin/pedidos/detalle/{id}
     */
    @GetMapping("/detalle/{id}")
    public String verDetallePedido(@PathVariable Long id, Model model) {
        
        // 1. Usamos el NUEVO método del servicio para buscar el pedido
        Pedido pedido = pedidoService.buscarPorId(id);
        
        if (pedido == null) {
            // (Opcional) Redirigir si el pedido no existe
            return "redirect:/admin/pedidos";
        }
        
        // 2. Pasamos el objeto Pedido completo a la vista
        model.addAttribute("pedido", pedido);
        
        // 3. Devolvemos el nombre del NUEVO archivo HTML que vamos a crear
        return "admin/detalle-pedido"; // <-- templates/admin/detalle-pedido.html
    }
    /**
     * Mapeo para actualizar el estado de un pedido desde el detalle.
     * URL: /admin/pedidos/actualizar-estado
     */
    @PostMapping("/actualizar-estado")
    public String actualizarEstadoPedido(@RequestParam Long pedidoId,
                                         @RequestParam String estado,
                                         RedirectAttributes redirectAttrs) {
        
        // 1. Buscamos el pedido por su ID (usando el método que ya creamos)
        Pedido pedido = pedidoService.buscarPorId(pedidoId);
        
        if (pedido != null) {
            // 2. Actualizamos solo el estado
            pedido.setEstado(estado);
            
            // 3. Guardamos el pedido actualizado (reutilizamos el método 'guardar')
            // (¡Asegúrate de que 'guardar' exista en PedidoService!)
            pedidoService.guardar(pedido); 
            
            redirectAttrs.addFlashAttribute("exito", "Estado del pedido actualizado correctamente.");
        } else {
            redirectAttrs.addFlashAttribute("error", "Error: No se encontró el pedido.");
        }
        
        // 4. Redirigimos de vuelta a la MISMA página de detalle
        return "redirect:/admin/pedidos/detalle/" + pedidoId;
    }

}