package com.mycompany.Food_restaurant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comprobante_detalle")
public class ComprobanteDetalle {

    @Id
    @Column(name = "CodComprobanteDetalle")
    private String codComprobanteDetalle;

    @Column(name = "CodComprobante")
    private String codComprobante;

    @Column(name = "CodAlimento")
    private String codAlimento;

    @Column(name = "Cantidad")
    private int cantidad;

    @Column(name = "Precio")
    private double precio;

    @Column(name = "DescuentoPromocion")
    private double descuentoPromocion;

    @Column(name = "DescuentoMonto")
    private double descuentoMonto;

    @Column(name = "SubTotal")
    private double subTotal;

    @Column(name = "UsuCreacion")
    private String usuCreacion;

    @Column(name = "FecCreacion")
    private LocalDateTime fecCreacion;

    @Column(name = "UsuModificacion")
    private String usuModificacion;

    @Column(name = "FecModificacion")
    private LocalDateTime fecModificacion;

    public String getCodComprobanteDetalle() { return codComprobanteDetalle; }
    public void setCodComprobanteDetalle(String v) { this.codComprobanteDetalle = v; }

    public String getCodComprobante() { return codComprobante; }
    public void setCodComprobante(String v) { this.codComprobante = v; }

    public String getCodAlimento() { return codAlimento; }
    public void setCodAlimento(String v) { this.codAlimento = v; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int v) { this.cantidad = v; }

    public double getPrecio() { return precio; }
    public void setPrecio(double v) { this.precio = v; }

    public double getDescuentoPromocion() { return descuentoPromocion; }
    public void setDescuentoPromocion(double v) { this.descuentoPromocion = v; }

    public double getDescuentoMonto() { return descuentoMonto; }
    public void setDescuentoMonto(double v) { this.descuentoMonto = v; }

    public double getSubTotal() { return subTotal; }
    public void setSubTotal(double v) { this.subTotal = v; }

    public String getUsuCreacion() { return usuCreacion; }
    public void setUsuCreacion(String v) { this.usuCreacion = v; }

    public LocalDateTime getFecCreacion() { return fecCreacion; }
    public void setFecCreacion(LocalDateTime v) { this.fecCreacion = v; }

    public String getUsuModificacion() { return usuModificacion; }
    public void setUsuModificacion(String v) { this.usuModificacion = v; }

    public LocalDateTime getFecModificacion() { return fecModificacion; }
    public void setFecModificacion(LocalDateTime v) { this.fecModificacion = v; }
}