
package com.mycompany.integradorpa2.persistencia;


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
    
    // ================= FAMILIA =================
    public void crearFamilia(com.mycompany.integradorpa2.logica.Familia f) {
        familiaJpa.create(f);
    }

    public void actualizarFamilia(com.mycompany.integradorpa2.logica.Familia f) {
        // Si tu FamiliaJpaController.edit lanza excepciones chequeadas, podés envolverlas acá
        try {
            familiaJpa.edit(f);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar la familia id=" + f.getId(), e);
        }
    }

    public void eliminarFamilia(int id) {
        try {
            familiaJpa.destroy(id);
        } catch (Exception e) { // p.ej. NonexistentEntityException, IllegalOrphanException (según tu implementación)
            throw new RuntimeException("No se pudo eliminar la familia id=" + id, e);
        }
    }

    public com.mycompany.integradorpa2.logica.Familia traerFamilia(int id) {
        return familiaJpa.findFamilia(id);
    }

    public java.util.List<com.mycompany.integradorpa2.logica.Familia> traerFamilias() {
        return familiaJpa.findFamiliaEntities();
    }

    // opcionales útiles
    public com.mycompany.integradorpa2.logica.Familia traerFamiliaPorEmail(String email) {
        return familiaJpa.findByEmail(email);
    }

    public java.util.List<com.mycompany.integradorpa2.logica.Familia> buscarFamiliasPorNombre(String patron) {
        return familiaJpa.searchByNombreLike(patron);
    }

    //========== HASTA ACA FAMILIA ==========
}

