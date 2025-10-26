package com.maysu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔗 Relación con la categoría del producto
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    // 📝 Datos básicos del producto
    private String nombre;
    private String descripcion;
    private double precio;
    private Double precioOferta; // ✅ Campo adicional para promociones
    private int stock;
    private String imagenUrl;

    // ✅ Nuevo campo: controla si el producto está disponible para la venta
    @Column(nullable = false)
    private boolean activo = true;

    // 🔧 Constructor vacío requerido por JPA
    public Producto() {}

    // 🔧 Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public Double getPrecioOferta() { return precioOferta; }
    public void setPrecioOferta(Double precioOferta) { this.precioOferta = precioOferta; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    // ✅ Getter y setter para el campo 'activo'
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
