package com.mycompany.integradorpa2.service;

import com.mycompany.integradorpa2.dao.*;
import com.mycompany.integradorpa2.logica.*;
import com.mycompany.integradorpa2.logica.enums.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AdopcionService {

    private final AdopcionDAO adopcionDao = new AdopcionDAOJpa();
    private final GatoDAO gatoDao = new GatoDAOJpa();
    private final FamiliaDAO familiaDao = new FamiliaDAOJpa();
    private final VoluntarioDAO voluntarioDao = new VoluntarioDAOJpa();
    private final SeguimientoDAO seguimientoDao = new SeguimientoDAOJpa();

    /* ============================================================
       FAMILIA: “postularse para adoptar” (crea solicitud EN_PROCESO)
       ============================================================ */
    public Adopcion postularFamiliaAGato(Long gatoId,
                                         Integer familiaId,
                                         TipoAdopcion tipo,
                                         String observacion) {
        Gato gato = gatoDao.buscarPorId(gatoId)
                .orElseThrow(() -> new IllegalArgumentException("Gato no encontrado: " + gatoId));
        if (Boolean.TRUE.equals(gato.isAdoptado())) {
            throw new IllegalStateException("El gato ya está adoptado.");
        }

        Familia familia = familiaDao.buscarPorId(familiaId)
                .orElseThrow(() -> new IllegalArgumentException("Familia no encontrada: " + familiaId));

        Adopcion a = new Adopcion();
        a.setGato(gato);
        a.setFamilia(familia);
        a.setTipoAdopcion(tipo);
        a.setEstado(EstadoAdopcion.EN_PROCESO);
        a.setFechaAdopcion(new Date());
        a.setObservacion(observacion);

        // persistir
        Adopcion creada = adopcionDao.crear(a);

        // enganchar como “adopción actual” del gato (pendiente)
        gato.setAdopcionActual(creada);
        gatoDao.actualizar(gato);

        return creada;
    }

    /* ===================================================================================
       VOLUNTARIO: asignar un gato a familia (equivale a crear solicitud EN_PROCESO)
       =================================================================================== */
    public Adopcion asignarGatoAFamilia(Long gatoId,
                                        Integer familiaId,
                                        TipoAdopcion tipo,
                                        String observacion) {
        // misma lógica que postular (puede usarse el mismo flujo)
        return postularFamiliaAGato(gatoId, familiaId, tipo, observacion);
    }

    /* ============================================================
       Aprobar adopción  -> estado APROBADA + marcar gato adoptado
       ============================================================ */
    public Adopcion aprobarAdopcion(Long adopcionId) {
        Adopcion a = adopcionDao.buscarPorId(adopcionId)
                .orElseThrow(() -> new IllegalArgumentException("Adopción no encontrada: " + adopcionId));

        if (a.getEstado() == EstadoAdopcion.APROBADA) return a;
        if (a.getGato() == null) throw new IllegalStateException("La adopción no tiene gato asociado.");

        // aprobar
        a.setEstado(EstadoAdopcion.APROBADA);
        Adopcion actualizada = adopcionDao.actualizar(a);

        // marcar gato como adoptado y colgar adopción actual
        Gato g = a.getGato();
        g.setAdoptado(true);
        g.setAdopcionActual(actualizada);
        gatoDao.actualizar(g);

        return actualizada;
    }

    /* ============================================================
       Rechazar adopción -> estado RECHAZADA + liberar gato
       ============================================================ */
    public Adopcion rechazarAdopcion(Long adopcionId, String motivo) {
        Adopcion a = adopcionDao.buscarPorId(adopcionId)
                .orElseThrow(() -> new IllegalArgumentException("Adopción no encontrada: " + adopcionId));

        a.setEstado(EstadoAdopcion.RECHAZADA);
        if (motivo != null && !motivo.isBlank()) {
            String obs = a.getObservacion();
            a.setObservacion((obs == null ? "" : (obs + " | ")) + "Motivo rechazo: " + motivo);
        }
        Adopcion actualizada = adopcionDao.actualizar(a);

        // si estaba colgada en el gato, liberarlo
        if (a.getGato() != null) {
            Gato g = a.getGato();
            g.setAdoptado(false);
            if (g.getAdopcionActual() != null &&
                g.getAdopcionActual().getId().equals(a.getId())) {
                g.setAdopcionActual(null);
            }
            gatoDao.actualizar(g);
        }
        return actualizada;
    }

    /* ============================================================
       Registrar VISITA DE SEGUIMIENTO (por voluntario)
       ============================================================ */
    public Seguimiento registrarSeguimiento(Long adopcionId,
                                            Integer voluntarioId,
                                            ResultadoSeguimiento resultado,
                                            String observacion,
                                            Date fechaHora) {
        Adopcion a = adopcionDao.buscarPorId(adopcionId)
                .orElseThrow(() -> new IllegalArgumentException("Adopción no encontrada: " + adopcionId));

        Voluntario v = voluntarioDao.buscarPorId(voluntarioId)
                .orElseThrow(() -> new IllegalArgumentException("Voluntario no encontrado: " + voluntarioId));

        if (a.getEstado() != EstadoAdopcion.APROBADA &&
            a.getEstado() != EstadoAdopcion.EN_PROCESO) {
            throw new IllegalStateException("No se puede registrar seguimiento para adopción en estado: " + a.getEstado());
        }

        Seguimiento s = new Seguimiento();
        s.setAdopcion(a);
        s.setVoluntario(v);
        s.setResultado(resultado);
        s.setObservacion(observacion);
        s.setFechaHora(fechaHora != null ? fechaHora : new Date());

        return seguimientoDao.crear(s);
    }

    /* ============================================================
       Listados y consultas de apoyo
       ============================================================ */
    public List<Gato> listarGatosDisponibles() {
        // si no tenés query custom, filtrá en memoria
        return gatoDao.listarTodos().stream()
                .filter(g -> !Boolean.TRUE.equals(g.isAdoptado()))
                .toList();
    }

    public List<Adopcion> listarAdopcionesDeFamilia(Integer familiaId) {
        Familia f = familiaDao.buscarPorId(familiaId)
                .orElseThrow(() -> new IllegalArgumentException("Familia no encontrada: " + familiaId));
        return adopcionDao.listarTodos().stream()
        .filter(a -> a.getFamilia() != null
                && a.getFamilia().getId() == f.getId())  
        .toList();
    }

    public List<Adopcion> listarAdopcionesDeGato(int gatoId) {
        return adopcionDao.listarTodos().stream()
                .filter(a -> a.getGato() != null && a.getGato().getId() == gatoId)
                .toList();
    }

    public Optional<Adopcion> adopcionPorId(Long id) {
        return adopcionDao.buscarPorId(id);
    }

    public List<Seguimiento> listarSeguimientosDeAdopcion(Long adopcionId) {
        // si no hay query específica en SeguimientoDAO, filtramos
        return seguimientoDao.listarTodos().stream()
                .filter(s -> s.getAdopcion() != null && s.getAdopcion().getId().equals(adopcionId))
                .toList();
    }

    public Familia marcarFamiliaDisponible(Integer familiaId) {
        Familia f = familiaDao.buscarPorId(familiaId)
                .orElseThrow(() -> new IllegalArgumentException("Familia no encontrada: " + familiaId));
        return familiaDao.actualizar(f);
    }
}
