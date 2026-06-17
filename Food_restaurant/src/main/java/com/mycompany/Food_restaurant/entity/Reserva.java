package com.mycompany.Food_restaurant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @Column(name = "CodReserva", length = 20)
    private String codReserva;

    @Column(name = "NroMesa", length = 10, nullable = false)
    private String nroMesa;

    @Column(name = "CodCliente", length = 20, nullable = false)
    private String codCliente;

    @Column(name = "FecReserva", nullable = false)
    private LocalDateTime fecReserva;

    @Column(name = "Estado", length = 3, nullable = false)
    private String estado = "ACT";

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
    public String getCodReserva() { return codReserva; }
    public void setCodReserva(String codReserva) { this.codReserva = codReserva; }

    public String getNroMesa() { return nroMesa; }
    public void setNroMesa(String nroMesa) { this.nroMesa = nroMesa; }

    public String getCodCliente() { return codCliente; }
    public void setCodCliente(String codCliente) { this.codCliente = codCliente; }

    public LocalDateTime getFecReserva() { return fecReserva; }
    public void setFecReserva(LocalDateTime fecReserva) { this.fecReserva = fecReserva; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUsuCreacion() { return usuCreacion; }
    public void setUsuCreacion(String usuCreacion) { this.usuCreacion = usuCreacion; }

    public LocalDateTime getFecCreacion() { return fecCreacion; }
    public void setFecCreacion(LocalDateTime fecCreacion) { this.fecCreacion = fecCreacion; }

    public String getUsuModificacion() { return usuModificacion; }
    public void setUsuModificacion(String usuModificacion) { this.usuModificacion = usuModificacion; }

    public LocalDateTime getFecModificacion() { return fecModificacion; }
    public void setFecModificacion(LocalDateTime fecModificacion) { this.fecModificacion = fecModificacion; }
}