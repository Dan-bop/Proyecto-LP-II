package com.maysu.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
     * Guarda el pedido completo con sus detalles desde el carrito.
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
     * Lista los pedidos del cliente para mostrar en la vista.
     */
    public List<Pedido> listarPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }
}
