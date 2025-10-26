package com.maysu.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maysu.model.Pedido;
import com.maysu.model.DetallePedido;
import com.maysu.model.ItemCarrito;
import com.maysu.model.Usuario;
import com.maysu.repository.PedidoRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    /**
     * (Cliente) Guarda el pedido completo con sus detalles desde el carrito.
     */
    public Pedido confirmarPedido(List<ItemCarrito> carrito, Usuario cliente) {
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDate.now());
        pedido.setUsuario(cliente);

        double total = 0;
        List<DetallePedido> detalles = new ArrayList<>();

        for (ItemCarrito item : carrito) {
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(item.getProducto());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getProducto().getPrecio());
            detalle.setSubtotal(item.getSubtotal());

            total += item.getSubtotal();
            detalles.add(detalle);
        }

        pedido.setTotal(total);
        pedido.setDetalles(detalles);

        return pedidoRepository.save(pedido); // guarda pedido + detalles
    }

    
    /**
     * (Cliente) Lista los pedidos del cliente para mostrar en la vista "Mi Cuenta".
     */
    public List<Pedido> listarPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    
    /**
     * (Genérico) Guarda un pedido ya construido.
     * Lo usa el checkout y la actualización de estado del admin.
     */
    public void guardar(Pedido pedido) {
        pedidoRepository.save(pedido);
    }

    /**
     * (Cliente) Busca el último pedido de un usuario (para la pág. de confirmación).
     */
    public Pedido buscarUltimoPedidoDe(Usuario usuario) {
        return pedidoRepository.findTopByUsuarioOrderByFechaDesc(usuario).orElse(null);
    }

    // --------------------------------------------------------------------
    // MÉTODOS AÑADIDOS PARA EL PANEL DE ADMINISTRACIÓN
    // --------------------------------------------------------------------

    /**
     * (Admin) Lista TODOS los pedidos de la base de datos.
     */
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    /**
     * (Admin) Busca un pedido específico por su ID.
     */
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    /**
     * (Admin) Lista solo los pedidos que coinciden con un estado.
     */
    public List<Pedido> listarPorEstado(String estado) {
        return pedidoRepository.findByEstado(estado);
    }
}