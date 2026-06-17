package com.mycompany.Food_restaurant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alimento")
public class Alimento {

    @Id
    @Column(name = "CodAlimento")
    private String codAlimento;

    @Column(name = "CodCategoria")
    private String codCategoria;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "Descripcion")
    private String descripcion;

    @Column(name = "Rutaimagen")
    private String rutaImagen;

    @Column(name = "DescuentoPromocion")
    private double descuentoPromocion;

    @Column(name = "Precio")
    private double precio;

    @Column(name = "Estado")
    private String estado;

    @Column(name = "UsuCreacion")
    private String usuCreacion;

    @Column(name = "FecCreacion")
    private LocalDateTime fecCreacion;

    @Column(name = "UsuModificacion")
    private String usuModificacion;

    @Column(name = "FecModificacion")
    private LocalDateTime fecModificacion;

    public String getCodAlimento() { return codAlimento; }
    public void setCodAlimento(String v) { this.codAlimento = v; }

    public String getCodCategoria() { return codCategoria; }
    public void setCodCategoria(String v) { this.codCategoria = v; }

    public String getNombre() { return nombre; }
    public void setNombre(String v) { this.nombre = v; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String v) { this.descripcion = v; }

    public String getRutaImagen() { return rutaImagen; }
    public void setRutaImagen(String v) { this.rutaImagen = v; }

    public double getDescuentoPromocion() { return descuentoPromocion; }
    public void setDescuentoPromocion(double v) { this.descuentoPromocion = v; }

    public double getPrecio() { return precio; }
    public void setPrecio(double v) { this.precio = v; }

    public String getEstado() { return estado; }
    public void setEstado(String v) { this.estado = v; }

    public String getUsuCreacion() { return usuCreacion; }
    public void setUsuCreacion(String v) { this.usuCreacion = v; }

    public LocalDateTime getFecCreacion() { return fecCreacion; }
    public void setFecCreacion(LocalDateTime v) { this.fecCreacion = v; }

    public String getUsuModificacion() { return usuModificacion; }
    public void setUsuModificacion(String v) { this.usuModificacion = v; }

    public LocalDateTime getFecModificacion() { return fecModificacion; }
    public void setFecModificacion(LocalDateTime v) { this.fecModificacion = v; }
}