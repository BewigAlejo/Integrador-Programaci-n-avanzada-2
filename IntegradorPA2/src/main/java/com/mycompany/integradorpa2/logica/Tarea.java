/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.integradorpa2.logica;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Usuario
 */
@Entity
public class Tarea {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Long id;
    @Basic
    private String descripcion;
    private String estadoTarea;   // p.ej. PENDIENTE / EN_PROCESO / HECHA
    private String tipoTarea;     // p.ej. VISITA, RESCATE, TRASLADO
  
    private String observacion;

    // relaciones
    
    @ManyToOne
    @JoinColumn(name = "voluntario_id")
    private Voluntario asignadaA;
    
    @ManyToOne
    @JoinColumn(name = "gato_id")
    private Gato gato;            // si la tarea se asocia a un gato
    
    @Temporal(TemporalType.DATE)
    private Date fecha;

    public Tarea(Long id, String descripcion, String estadoTarea, String tipoTarea, String observacion, Voluntario asignadaA, Gato gato, Date fecha) {
        this.id = id;
        this.descripcion = descripcion;
        this.estadoTarea = estadoTarea;
        this.tipoTarea = tipoTarea;
        this.observacion = observacion;
        this.asignadaA = asignadaA;
        this.gato = gato;
        this.fecha = fecha;
    }

    public Tarea() {}
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstadoTarea() {
        return estadoTarea;
    }

    public void setEstadoTarea(String estadoTarea) {
        this.estadoTarea = estadoTarea;
    }

    public String getTipoTarea() {
        return tipoTarea;
    }

    public void setTipoTarea(String tipoTarea) {
        this.tipoTarea = tipoTarea;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Voluntario getAsignadaA() {
        return asignadaA;
    }

    public void setAsignadaA(Voluntario asignadaA) {
        this.asignadaA = asignadaA;
    }

    public Gato getGato() {
        return gato;
    }

    public void setGato(Gato gato) {
        this.gato = gato;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    
}
