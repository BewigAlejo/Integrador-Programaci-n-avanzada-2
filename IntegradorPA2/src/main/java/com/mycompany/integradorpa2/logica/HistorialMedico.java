/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.integradorpa2.logica;

import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Usuario
 */
@Entity
public class HistorialMedico {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Long id;
  
    // relaciones
    
    @OneToOne
    @JoinColumn(name = "gato_id")
    private Gato gato;
    
    @OneToMany(mappedBy = "historial", cascade = CascadeType.ALL)
    private List<EntradaHistorial> entradas;
    
    @Temporal(TemporalType.DATE)
    private Date fechaApertura;

    public HistorialMedico(Long id, Gato gato, List<EntradaHistorial> entradas, Date fechaApertura) {
        this.id = id;
        this.gato = gato;
        this.entradas = entradas;
        this.fechaApertura = fechaApertura;
    }

    public HistorialMedico() {}
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Gato getGato() {
        return gato;
    }

    public void setGato(Gato gato) {
        this.gato = gato;
    }

    public List<EntradaHistorial> getEntradas() {
        return entradas;
    }

    public void setEntradas(List<EntradaHistorial> entradas) {
        this.entradas = entradas;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }
    
    
}
