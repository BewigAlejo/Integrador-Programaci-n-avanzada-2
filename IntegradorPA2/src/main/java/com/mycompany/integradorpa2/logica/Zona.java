/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.integradorpa2.logica;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Usuario
 */
@Entity
public class Zona {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Long id;
    @Basic
    private String nombreZona;
    private Double coor_lat , coor_lon;

    // relaciones
    @OneToMany(mappedBy = "zona", cascade = CascadeType.ALL)
    private List<Gato> gatos;
    
    @OneToMany(mappedBy = "zonaAsignada", cascade = CascadeType.ALL)
    private List<Voluntario> voluntarios;

    public Zona(Long id, String nombreZona, Double coor_lat, Double coor_lon, List<Gato> gatos, List<Voluntario> voluntarios) {
        this.id = id;
        this.nombreZona = nombreZona;
        this.coor_lat = coor_lat;
        this.coor_lon = coor_lon;
        this.gatos = gatos;
        this.voluntarios = voluntarios;
    }

    public Zona() {}

        @Override
    public String toString() {
        return this.getNombreZona(); // o el campo que corresponda
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public Double getCoor_lat() {
        return coor_lat;
    }

    public void setCoor_lat(Double coor_lat) {
        this.coor_lat = coor_lat;
    }

    public Double getCoor_lon() {
        return coor_lon;
    }

    public void setCoor_lon(Double coor_lon) {
        this.coor_lon = coor_lon;
    }

    public List<Gato> getGatos() {
        return gatos;
    }

    public void setGatos(List<Gato> gatos) {
        this.gatos = gatos;
    }

    public List<Voluntario> getVoluntarios() {
        return voluntarios;
    }

    public void setVoluntarios(List<Voluntario> voluntarios) {
        this.voluntarios = voluntarios;
    }
    
    
}
