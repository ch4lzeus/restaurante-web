package com.mycompany.Food_restaurant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @Column(name = "CodCliente", length = 20)
    private String codCliente;

    @Column(name = "CodUsuario", length = 20)
    private String codUsuario;

    @Column(name = "CodTipoDocumento", length = 10, nullable = false)
    private String codTipoDocumento;

    @Column(name = "NroDocumento", length = 20, nullable = false)
    private String nroDocumento;

    @Column(name = "RazonSocial", length = 250, nullable = false)
    private String razonSocial;

    @Column(name = "Direccion", length = 250)
    private String direccion;

    @Column(name = "Telefono", length = 20)
    private String telefono;

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
    public String getCodCliente() { return codCliente; }
    public void setCodCliente(String codCliente) { this.codCliente = codCliente; }

    public String getCodUsuario() { return codUsuario; }
    public void setCodUsuario(String codUsuario) { this.codUsuario = codUsuario; }

    public String getCodTipoDocumento() { return codTipoDocumento; }
    public void setCodTipoDocumento(String codTipoDocumento) { this.codTipoDocumento = codTipoDocumento; }

    public String getNroDocumento() { return nroDocumento; }
    public void setNroDocumento(String nroDocumento) { this.nroDocumento = nroDocumento; }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getUsuCreacion() { return usuCreacion; }
    public void setUsuCreacion(String usuCreacion) { this.usuCreacion = usuCreacion; }

    public LocalDateTime getFecCreacion() { return fecCreacion; }
    public void setFecCreacion(LocalDateTime fecCreacion) { this.fecCreacion = fecCreacion; }

    public String getUsuModificacion() { return usuModificacion; }
    public void setUsuModificacion(String usuModificacion) { this.usuModificacion = usuModificacion; }

    public LocalDateTime getFecModificacion() { return fecModificacion; }
    public void setFecModificacion(LocalDateTime fecModificacion) { this.fecModificacion = fecModificacion; }
}