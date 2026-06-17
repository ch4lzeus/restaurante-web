package com.mycompany.Food_restaurant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "correlativo_comprobante")
public class CorrelativoComprobante {

    @Id
    @Column(name = "CodCorrelativoComprobante")
    private String codCorrelativoComprobante;

    @Column(name = "CodTipoComprobante")
    private String codTipoComprobante;

    @Column(name = "Serie")
    private String serie;

    @Column(name = "Correlativo")
    private int correlativo;

    @Column(name = "CorrelativoActual")
    private int correlativoActual;

    @Column(name = "UsuCreacion")
    private String usuCreacion;

    @Column(name = "FecCreacion")
    private LocalDateTime fecCreacion;

    @Column(name = "UsuModificacion")
    private String usuModificacion;

    @Column(name = "FecModificacion")
    private LocalDateTime fecModificacion;

    public String getCodCorrelativoComprobante() { return codCorrelativoComprobante; }
    public void setCodCorrelativoComprobante(String v) { this.codCorrelativoComprobante = v; }

    public String getCodTipoComprobante() { return codTipoComprobante; }
    public void setCodTipoComprobante(String v) { this.codTipoComprobante = v; }

    public String getSerie() { return serie; }
    public void setSerie(String v) { this.serie = v; }

    public int getCorrelativo() { return correlativo; }
    public void setCorrelativo(int v) { this.correlativo = v; }

    public int getCorrelativoActual() { return correlativoActual; }
    public void setCorrelativoActual(int v) { this.correlativoActual = v; }

    public String getUsuCreacion() { return usuCreacion; }
    public void setUsuCreacion(String v) { this.usuCreacion = v; }

    public LocalDateTime getFecCreacion() { return fecCreacion; }
    public void setFecCreacion(LocalDateTime v) { this.fecCreacion = v; }

    public String getUsuModificacion() { return usuModificacion; }
    public void setUsuModificacion(String v) { this.usuModificacion = v; }

    public LocalDateTime getFecModificacion() { return fecModificacion; }
    public void setFecModificacion(LocalDateTime v) { this.fecModificacion = v; }
}