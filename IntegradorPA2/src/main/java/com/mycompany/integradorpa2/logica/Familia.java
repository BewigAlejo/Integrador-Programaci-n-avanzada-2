/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.integradorpa2.logica;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author Usuario
 */
@Entity
public class Familia extends Usuario {
    private String direccion;
    private String coordenadas;
    private Integer reputacion;
    
    // relaciones
    
    @OneToMany(mappedBy = "familia", cascade = CascadeType.ALL)
    private List<Adopcion> adopciones;

    public Familia(int id, String nombre, String email, String telefono, String rol, String usuario, String contrasenia) {
        super(id, nombre, email, telefono, rol, usuario, contrasenia);
    }
    
    

    public Familia() {
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public Integer getReputacion() {
        return reputacion;
    }

    public void setReputacion(Integer reputacion) {
        this.reputacion = reputacion;
    }

    public List getAdopciones() {
        return adopciones;
    }

    public void setAdopciones(List<Adopcion> adopciones) {
        this.adopciones = adopciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    
}
