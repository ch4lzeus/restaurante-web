package com.mycompany.Food_restaurant.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @Column(name = "CodUsuario", length = 20)
    private String codUsuario;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "contrasena")
    private String contrasena;

    @Column(name = "estado")
    private String estado;

    @Column(name = "CodRol")
    private String codRol;

    // Getters y Setters
    public String getCodUsuario() { return codUsuario; }
    public void setCodUsuario(String codUsuario) { this.codUsuario = codUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCodRol() { return codRol; }
    public void setCodRol(String codRol) { this.codRol = codRol; }
}