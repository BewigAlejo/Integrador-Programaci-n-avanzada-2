/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.integradorpa2.logica;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Usuario
 */

@Entity
public class Adopcion implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Long id;
  
    @Basic
    private String tipoAdopcion;  // p.ej. TEMPORAL / DEFINITIVA
    private String estado;        // p.ej. EN_PROCESO / APROBADA / RECHAZADA
    private String observacion;

    // relaciones
    @ManyToOne
    @JoinColumn(name = "familia_id")
    private Familia familia;
    
    @OneToOne
    @JoinColumn(name = "gato_id")
    private Gato gato;
    
    @Temporal(TemporalType.DATE)
    private Date fechaAdopcion;

    public Adopcion() {}
    
    public Adopcion(Long id, String tipoAdopcion, String estado, String observacion, Familia familia, Gato gato, Date fechaAdopcion) {
        this.id = id;
        this.tipoAdopcion = tipoAdopcion;
        this.estado = estado;
        this.observacion = observacion;
        this.familia = familia;
        this.gato = gato;
        this.fechaAdopcion = fechaAdopcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoAdopcion() {
        return tipoAdopcion;
    }

    public void setTipoAdopcion(String tipoAdopcion) {
        this.tipoAdopcion = tipoAdopcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Familia getFamilia() {
        return familia;
    }

    public void setFamilia(Familia familia) {
        this.familia = familia;
    }

    public Gato getGato() {
        return gato;
    }

    public void setGato(Gato gato) {
        this.gato = gato;
    }

    public Date getFechaAdopcion() {
        return fechaAdopcion;
    }

    public void setFechaAdopcion(Date fechaAdopcion) {
        this.fechaAdopcion = fechaAdopcion;
    }
    
    
}
