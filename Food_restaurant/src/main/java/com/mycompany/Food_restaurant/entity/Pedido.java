package com.mycompany.Food_restaurant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @Column(name = "NroPedido", length = 20)
    private String nroPedido;

    @Column(name = "NroMesa", length = 10)
    private String nroMesa;

    @Column(name = "CodCliente", length = 20, nullable = false)
    private String codCliente;

    @Column(name = "EsLocal", nullable = false)
    private int esLocal = 1;

    @Column(name = "Direccion", length = 250)
    private String direccion;

    @Column(name = "Telefono", length = 20)
    private String telefono;

    @Column(name = "Descripcion", length = 500)
    private String descripcion;

    @Column(name = "Estado", length = 20, nullable = false)
    private String estado = "PENDIENTE";

    @Column(name = "FecPedido", nullable = false)
    private LocalDateTime fecPedido;

    @Column(name = "Total", nullable = false)
    private double total = 0;

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
        if (fecPedido == null) fecPedido = LocalDateTime.now();
    }

    // Getters y Setters
    public String getNroPedido() { return nroPedido; }
    public void setNroPedido(String nroPedido) { this.nroPedido = nroPedido; }

    public String getNroMesa() { return nroMesa; }
    public void setNroMesa(String nroMesa) { this.nroMesa = nroMesa; }

    public String getCodCliente() { return codCliente; }
    public void setCodCliente(String codCliente) { this.codCliente = codCliente; }

    public int getEsLocal() { return esLocal; }
    public void setEsLocal(int esLocal) { this.esLocal = esLocal; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFecPedido() { return fecPedido; }
    public void setFecPedido(LocalDateTime fecPedido) { this.fecPedido = fecPedido; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getUsuCreacion() { return usuCreacion; }
    public void setUsuCreacion(String usuCreacion) { this.usuCreacion = usuCreacion; }

    public LocalDateTime getFecCreacion() { return fecCreacion; }
    public void setFecCreacion(LocalDateTime fecCreacion) { this.fecCreacion = fecCreacion; }

    public String getUsuModificacion() { return usuModificacion; }
    public void setUsuModificacion(String usuModificacion) { this.usuModificacion = usuModificacion; }

    public LocalDateTime getFecModificacion() { return fecModificacion; }
    public void setFecModificacion(LocalDateTime fecModificacion) { this.fecModificacion = fecModificacion; }
}