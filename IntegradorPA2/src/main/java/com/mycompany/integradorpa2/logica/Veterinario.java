/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.integradorpa2.logica;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author Usuario
 */

@Entity
public class Veterinario extends Usuario {
    @Basic
    private String matricula;
    private String especialidad;

    // relaciones
    @OneToMany(mappedBy = "veterinario", cascade = CascadeType.ALL)
    private List<EntradaHistorial> entradasGeneradas;

    public Veterinario(int id, String nombre, String email, String telefono, String rol) {
        super(id, nombre, email, telefono, rol);
    }

    public Veterinario() {
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public List getEntradasGeneradas() {
        return entradasGeneradas;
    }

    public void setEntradasGeneradas(List entradasGeneradas) {
        this.entradasGeneradas = entradasGeneradas;
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
