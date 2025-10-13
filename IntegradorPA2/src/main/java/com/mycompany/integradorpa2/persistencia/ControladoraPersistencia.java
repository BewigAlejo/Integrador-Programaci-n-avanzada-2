/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.integradorpa2.persistencia;

/**
 *
 * @author Usuario
 */

import com.mycompany.integradorpa2.persistencia.AdopcionJpaController;
import com.mycompany.integradorpa2.persistencia.EntradaHistorialJpaController;
import com.mycompany.integradorpa2.persistencia.FamiliaJpaController;
import com.mycompany.integradorpa2.persistencia.GatoJpaController;
import com.mycompany.integradorpa2.persistencia.HistorialMedicoJpaController;
import com.mycompany.integradorpa2.persistencia.TareaJpaController;
import com.mycompany.integradorpa2.persistencia.VeterinarioJpaController;
import com.mycompany.integradorpa2.persistencia.VoluntarioJpaController;
import com.mycompany.integradorpa2.persistencia.ZonaJpaController;

public class ControladoraPersistencia {

    // Instancias de todos los controllers
    public final AdopcionJpaController adopcionJpa;
    public final EntradaHistorialJpaController entradaHistorialJpa;
    public final FamiliaJpaController familiaJpa;
    public final GatoJpaController gatoJpa;
    public final HistorialMedicoJpaController historialMedicoJpa;
    public final TareaJpaController tareaJpa;
    public final VeterinarioJpaController veterinarioJpa;
    public final VoluntarioJpaController voluntarioJpa;
    public final ZonaJpaController zonaJpa;

    public ControladoraPersistencia() {
        this.adopcionJpa = new AdopcionJpaController();
        this.entradaHistorialJpa = new EntradaHistorialJpaController();
        this.familiaJpa = new FamiliaJpaController();
        this.gatoJpa = new GatoJpaController();
        this.historialMedicoJpa = new HistorialMedicoJpaController();
        this.tareaJpa = new TareaJpaController();
        this.veterinarioJpa = new VeterinarioJpaController();
        this.voluntarioJpa = new VoluntarioJpaController();
        this.zonaJpa = new ZonaJpaController();
    }
}

