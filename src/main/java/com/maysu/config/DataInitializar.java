package com.maysu.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.maysu.model.Rol;
import com.maysu.model.Usuario;
import com.maysu.repository.RolRepository;
import com.maysu.repository.UsuarioRepository;

@Configuration
public class DataInitializar implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear roles si no existen
        if (rolRepository.findByNombre("ROLE_CLIENTE").isEmpty()) {
            rolRepository.save(new Rol("ROLE_CLIENTE"));
        }
        if (rolRepository.findByNombre("ROLE_EMPLEADO").isEmpty()) {
            rolRepository.save(new Rol("ROLE_EMPLEADO"));
        }
        if (rolRepository.findByNombre("ROLE_ADMIN").isEmpty()) {
            rolRepository.save(new Rol("ROLE_ADMIN"));
        }

        // Crear usuario administrador inicial si no existe
        if (usuarioRepository.findByEmail("adminMaysu@maysu.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setEmail("adminMaysu@maysu.com");
            admin.setNombre("Admin");
            admin.setApellido("Maysu");
            // Contraseña encriptada con BCrypt
            admin.setPassword(passwordEncoder.encode("Maysu"));

            Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN").get();
            admin.setRoles(Set.of(rolAdmin));

            usuarioRepository.save(admin);

            System.out.println("✅ Usuario administrador creado: adminMaysu@maysu.com / Maysu");
        }
    }
}
