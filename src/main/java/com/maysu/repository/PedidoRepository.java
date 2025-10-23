package com.maysu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.maysu.model.Pedido;
import com.maysu.model.Usuario;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioId(Long usuarioId);

    // ✅ Método para obtener el último pedido por fecha
    Optional<Pedido> findTopByUsuarioOrderByFechaDesc(Usuario usuario);
}
