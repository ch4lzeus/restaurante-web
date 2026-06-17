package com.mycompany.Food_restaurant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tipo_comprobante")
public class TipoComprobante {

    @Id
    @Column(name = "CodTipoComprobante")
    private String codTipoComprobante;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "UsuCreacion")
    private String usuCreacion;

    @Column(name = "FecCreacion")
    private LocalDateTime fecCreacion;

    public String getCodTipoComprobante() { return codTipoComprobante; }
    public void setCodTipoComprobante(String v) { this.codTipoComprobante = v; }

    public String getNombre() { return nombre; }
    public void setNombre(String v) { this.nombre = v; }

    public String getUsuCreacion() { return usuCreacion; }
    public void setUsuCreacion(String v) { this.usuCreacion = v; }

    public LocalDateTime getFecCreacion() { return fecCreacion; }
    public void setFecCreacion(LocalDateTime v) { this.fecCreacion = v; }
}