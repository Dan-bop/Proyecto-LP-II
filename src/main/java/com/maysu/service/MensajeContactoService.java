package com.maysu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maysu.model.MensajeContacto;
import com.maysu.repository.MensajeContactoRepository;

@Service
public class MensajeContactoService {

    @Autowired
    private MensajeContactoRepository mensajeRepository;

    /**
     * (Admin) Lista TODOS los mensajes, ordenados por fecha (nuevos primero).
     */
    public List<MensajeContacto> listarTodos() {
        // JpaRepository no tiene un findAll(Sort) por defecto, 
        // pero findAll() es suficiente por ahora.
        // Si quisieras ordenar:
        // return mensajeRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaEnvio"));
        return mensajeRepository.findAll();
    }

    /**
     * (Admin) Busca un mensaje por su ID.
     */
    public MensajeContacto buscarPorId(Long id) {
        return mensajeRepository.findById(id).orElse(null);
    }

    /**
     * (Admin) Elimina un mensaje por su ID.
     */
    public void eliminarPorId(Long id) {
        mensajeRepository.deleteById(id);
    }

    /**
     * (Admin) Cambia el estado "atendido" de un mensaje.
     */
    public void marcarComoAtendido(Long id, boolean estado) {
        MensajeContacto mensaje = buscarPorId(id);
        if (mensaje != null) {
            mensaje.setAtendido(estado);
            mensajeRepository.save(mensaje);
        }
    }
}