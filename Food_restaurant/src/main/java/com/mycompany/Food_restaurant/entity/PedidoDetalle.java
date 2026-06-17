package com.mycompany.Food_restaurant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido_detalle")
public class PedidoDetalle {

    @Id
    @Column(name = "CodPedidoDetalle", length = 20)
    private String codPedidoDetalle;

    @Column(name = "NroPedido", length = 20, nullable = false)
    private String nroPedido;

    @Column(name = "CodAlimento", length = 20, nullable = false)
    private String codAlimento;

    @Column(name = "Cantidad", nullable = false)
    private int cantidad;

    @Column(name = "Precio", nullable = false)
    private double precio;

    @Column(name = "DescuentoPromocion")
    private double descuentoPromocion = 0;

    @Column(name = "DescuentoMonto")
    private double descuentoMonto = 0;

    @Column(name = "SubTotal", nullable = false)
    private double subTotal;

    @Column(name = "UsuCreacion", length = 20, nullable = false)
    private String usuCreacion = "dbo";

    @Column(name = "FecCreacion", nullable = false)
    private LocalDateTime fecCreacion;

    @Column(name = "UsuModificacion", length = 20)
    private String usuModificacion;

    @Column(name = "FecModificacion")
    private LocalDateTime fecModificacion;

    @PrePersist
    public void prePersist() {
        if (fecCreacion == null) fecCreacion = LocalDateTime.now();
    }

    // Getters y Setters
    public String getCodPedidoDetalle() { return codPedidoDetalle; }
    public void setCodPedidoDetalle(String codPedidoDetalle) { this.codPedidoDetalle = codPedidoDetalle; }

    public String getNroPedido() { return nroPedido; }
    public void setNroPedido(String nroPedido) { this.nroPedido = nroPedido; }

    public String getCodAlimento() { return codAlimento; }
    public void setCodAlimento(String codAlimento) { this.codAlimento = codAlimento; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public double getDescuentoPromocion() { return descuentoPromocion; }
    public void setDescuentoPromocion(double descuentoPromocion) { this.descuentoPromocion = descuentoPromocion; }

    public double getDescuentoMonto() { return descuentoMonto; }
    public void setDescuentoMonto(double descuentoMonto) { this.descuentoMonto = descuentoMonto; }

    public double getSubTotal() { return subTotal; }
    public void setSubTotal(double subTotal) { this.subTotal = subTotal; }

    public String getUsuCreacion() { return usuCreacion; }
    public void setUsuCreacion(String usuCreacion) { this.usuCreacion = usuCreacion; }

    public LocalDateTime getFecCreacion() { return fecCreacion; }
    public void setFecCreacion(LocalDateTime fecCreacion) { this.fecCreacion = fecCreacion; }

    public String getUsuModificacion() { return usuModificacion; }
    public void setUsuModificacion(String usuModificacion) { this.usuModificacion = usuModificacion; }

    public LocalDateTime getFecModificacion() { return fecModificacion; }
    public void setFecModificacion(LocalDateTime fecModificacion) { this.fecModificacion = fecModificacion; }
}