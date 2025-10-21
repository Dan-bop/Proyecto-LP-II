package com.maysu.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.maysu.model.Usuario;
import com.maysu.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ✅ Método requerido por Spring Security para cargar el usuario por su email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 🔍 Buscar el usuario en la base de datos por email
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // ✅ Convertir los roles del usuario en autoridades para Spring Security
        Set<GrantedAuthority> authorities = usuario.getRoles().stream()
            .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
            .collect(Collectors.toSet());

        // ✅ Solución: devolver directamente el objeto `Usuario`, que implementa `UserDetails`
        // Esto permite acceder a `nombre` y `apellido` desde el menú con `#authentication.principal`
        usuario.setAuthorities(authorities); // 👈 Si usas un campo para guardar las autoridades
        return usuario;
    }

    // ✅ Método auxiliar para buscar usuario por email
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
    }

    // ✅ Método para guardar un nuevo usuario
    public void guardar(Usuario usuario) {
        usuarioRepository.save(usuario);
    }
}
