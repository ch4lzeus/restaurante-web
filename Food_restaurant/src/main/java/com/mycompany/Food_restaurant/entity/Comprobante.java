package com.mycompany.Food_restaurant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comprobante")
public class Comprobante {

    @Id
    @Column(name = "CodComprobante")
    private String codComprobante;

    @Column(name = "CodTipoComprobante")
    private String codTipoComprobante;

    @Column(name = "NroPedido")
    private String nroPedido;

    @Column(name = "CodCliente")
    private String codCliente;

    @Column(name = "NroComprobante")
    private String nroComprobante;

    @Column(name = "Estado")
    private String estado;

    @Column(name = "FecEmision")
    private LocalDateTime fecEmision;

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

    public String getCodTipoComprobante() { return codTipoComprobante; }
    public void setCodTipoComprobante(String v) { this.codTipoComprobante = v; }

    public String getNroPedido() { return nroPedido; }
    public void setNroPedido(String v) { this.nroPedido = v; }

    public String getCodCliente() { return codCliente; }
    public void setCodCliente(String v) { this.codCliente = v; }

    public String getNroComprobante() { return nroComprobante; }
    public void setNroComprobante(String v) { this.nroComprobante = v; }

    public String getEstado() { return estado; }
    public void setEstado(String v) { this.estado = v; }

    public LocalDateTime getFecEmision() { return fecEmision; }
    public void setFecEmision(LocalDateTime v) { this.fecEmision = v; }

    public String getUsuCreacion() { return usuCreacion; }
    public void setUsuCreacion(String v) { this.usuCreacion = v; }

    public LocalDateTime getFecCreacion() { return fecCreacion; }
    public void setFecCreacion(LocalDateTime v) { this.fecCreacion = v; }

    public String getUsuModificacion() { return usuModificacion; }
    public void setUsuModificacion(String v) { this.usuModificacion = v; }

    public LocalDateTime getFecModificacion() { return fecModificacion; }
    public void setFecModificacion(LocalDateTime v) { this.fecModificacion = v; }
}