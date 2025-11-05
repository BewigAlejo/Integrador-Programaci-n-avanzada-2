/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.integradorpa2.logica;
import com.mycompany.integradorpa2.logica.Zona;
import com.mycompany.integradorpa2.logica.enums.Disponibilidad;
import com.mycompany.integradorpa2.logica.enums.Experiencia;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Usuario
 */

@Entity
public class Voluntario extends Usuario {
    @Enumerated(EnumType.STRING)
    private Disponibilidad disponibilidad;
    
    @Enumerated(EnumType.STRING)
    private Experiencia experiencia;
    
    // relaciones
    
    @ManyToOne
    @JoinColumn(name = "zona_id")
    private Zona zonaAsignada;
    
    @OneToMany(mappedBy = "asignadaA", cascade = CascadeType.ALL)
    private List<Tarea> tareasAsignadas;

    public Voluntario(int id, String nombre, String email, String telefono, String rol, String usuario, String contrasenia) {
        super(id, nombre, email, telefono, rol, usuario , contrasenia);
    }

    public Voluntario() {
    }

    public Disponibilidad getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Disponibilidad disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public Experiencia getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(Experiencia experiencia) {
        this.experiencia = experiencia;
    }

    public Zona getZonaAsignada() {
        return zonaAsignada;
    }

    public void setZonaAsignada(Zona zonaAsignada) {
        this.zonaAsignada = zonaAsignada;
    }

    public List getTareasAsignadas() {
        return tareasAsignadas;
    }

    public void setTareasAsignadas(List tareasAsignadas) {
        this.tareasAsignadas = tareasAsignadas;
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
