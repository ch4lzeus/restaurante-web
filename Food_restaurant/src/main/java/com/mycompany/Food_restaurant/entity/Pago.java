package com.mycompany.Food_restaurant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @Column(name = "CodComprobante")
    private String codComprobante;

    @Column(name = "CodMetodoPago")
    private String codMetodoPago;

    @Column(name = "Monto")
    private double monto;

    @Column(name = "UsuCreacion")
    private String usuCreacion;

    @Column(name = "FecCreacion")
    private LocalDateTime fecCreacion;

    @Column(name = "UsuModificacion")
    private String usuModificacion;

    @Column(name = "FecModificacion")
    private LocalDateTime fecModificacion;

    public String getCodComprobante() { return codComprobante; }
    public void setCodComprobante(String v) { this.codComprobante = v; }

    public String getCodMetodoPago() { return codMetodoPago; }
    public void setCodMetodoPago(String v) { this.codMetodoPago = v; }

    public double getMonto() { return monto; }
    public void setMonto(double v) { this.monto = v; }

    public String getUsuCreacion() { return usuCreacion; }
    public void setUsuCreacion(String v) { this.usuCreacion = v; }

    public LocalDateTime getFecCreacion() { return fecCreacion; }
    public void setFecCreacion(LocalDateTime v) { this.fecCreacion = v; }

    public String getUsuModificacion() { return usuModificacion; }
    public void setUsuModificacion(String v) { this.usuModificacion = v; }

    public LocalDateTime getFecModificacion() { return fecModificacion; }
    public void setFecModificacion(LocalDateTime v) { this.fecModificacion = v; }
}