package com.maysu.config;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 1. Obtenemos los roles (authorities) del usuario
        Set<String> roles = authentication.getAuthorities().stream()
                             .map(GrantedAuthority::getAuthority)
                             .collect(Collectors.toSet());

        // 2. Comparamos y decidimos la redirección
        if (roles.contains("ROLE_ADMIN")) {
            // Si es ADMIN, lo mandamos al panel
            response.sendRedirect("/admin");
        } else if (roles.contains("ROLE_CLIENTE")) {
            // Si es CLIENTE, lo mandamos al inicio (home)
            response.sendRedirect("/");
        } else {
            // Si es otro rol (ej. EMPLEADO), lo mandamos al inicio
            response.sendRedirect("/");
        }
    }
}