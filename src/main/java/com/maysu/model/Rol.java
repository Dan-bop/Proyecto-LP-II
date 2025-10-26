package com.maysu.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles") // Define la tabla en la base de datos
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Clave primaria autogenerada
    private Long id;

    @Column(unique = true, nullable = false) // Evita duplicados y asegura integridad
    private String nombre; // Ejemplo: "ROLE_CLIENTE", "ROLE_EMPLEADO"

    @ManyToMany(mappedBy = "roles") // Relación inversa con Usuario
    private Set<Usuario> usuarios;

    public Rol() {}

    // Constructor adicional para inicializar roles rápidamente
    public Rol(String nombre) {
        this.nombre = nombre;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Set<Usuario> getUsuarios() { return usuarios; }
    public void setUsuarios(Set<Usuario> usuarios) { this.usuarios = usuarios; }
}
