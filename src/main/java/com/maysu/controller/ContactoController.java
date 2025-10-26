package com.maysu.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.maysu.model.MensajeContacto;
import com.maysu.repository.MensajeContactoRepository;

//Ruta que carga la vista de Contacto
@Controller
public class ContactoController {
	
	@Autowired
    private MensajeContactoRepository mensajeRepo;

    // Muestra el formulario de contacto
    @GetMapping("/contacto")
    public String mostrarFormulario(Model model) {
        model.addAttribute("mensajeContacto", new MensajeContacto());
        return "contacto";
    }

    // Procesa y guarda el mensaje enviado
    @PostMapping("/contacto")
    public String procesarFormulario(@ModelAttribute MensajeContacto mensajeContacto, Model model) {
        mensajeContacto.setFechaEnvio(LocalDateTime.now());
        mensajeRepo.save(mensajeContacto);
        model.addAttribute("exito", "Gracias por tu mensaje, " + mensajeContacto.getNombre() + ". Lo hemos registrado correctamente.");
        model.addAttribute("mensajeContacto", new MensajeContacto()); // limpia el formulario
        return "contacto";
    }
}


