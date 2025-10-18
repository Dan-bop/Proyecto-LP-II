package com.maysu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.maysu.model.Rol;
import com.maysu.repository.RolRepository;

@Component
public class DataInitializar implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public void run(String... args) throws Exception {
        if (rolRepository.findByNombre("ROLE_CLIENTE").isEmpty()) {
            rolRepository.save(new Rol("ROLE_CLIENTE"));
        }
        if (rolRepository.findByNombre("ROLE_EMPLEADO").isEmpty()) {
            rolRepository.save(new Rol("ROLE_EMPLEADO"));
        }
    }
}

