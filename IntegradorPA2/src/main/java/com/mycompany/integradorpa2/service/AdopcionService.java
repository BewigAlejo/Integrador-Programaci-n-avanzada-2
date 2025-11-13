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

        if (a.getGato() == null) {
            throw new IllegalStateException("La adopción no tiene gato asociado.");
        }

        // Si ya está aprobada, devuelvo
        if (a.getEstado() == EstadoAdopcion.APROBADA) {
            return a;
        }

        // 1) Aprobar la adopción seleccionada
        a.setEstado(EstadoAdopcion.APROBADA);
        Adopcion actualizada = adopcionDao.actualizar(a);

        // 2) Marcar el gato como adoptado
        Gato g = a.getGato();
        g.setAdoptado(true);
        g.setAdopcionActual(actualizada);
        gatoDao.actualizar(g);

        // 3) Rechazar TODAS las otras adopciones EN_PROCESO del mismo gato
        adopcionDao.listarTodos().stream()
                .filter(ot -> ot.getId() != null && !ot.getId().equals(actualizada.getId()))
                .filter(ot -> ot.getGato() != null && ot.getGato().getId().equals(g.getId()))
                .filter(ot -> ot.getEstado() == EstadoAdopcion.EN_PROCESO)
                .forEach(ot -> {
                    ot.setEstado(EstadoAdopcion.RECHAZADA);
                    adopcionDao.actualizar(ot);
                });

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

    public List<Adopcion> listarAdopcionesDeGato(Long gatoId) {
    return adopcionDao.listarTodos().stream()
            .filter(a -> a.getGato() != null
                      && a.getGato().getId() != null
                      && a.getGato().getId().equals(gatoId))
            .toList();
}

    public List<Adopcion> listarAdopcionesDeFamilia(Integer familiaId) {
    return adopcionDao.listarTodos().stream()
            .filter(a -> a.getFamilia() != null
                      && a.getFamilia().getId() == familiaId)  
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
    
        // ============================================================
    // Adopciones EN_PROCESO (para asignar gato / seguimiento)
    // ============================================================
    public List<Adopcion> listarAdopcionesEnProceso() {
        return adopcionDao.listarTodos().stream()
                .filter(a -> a.getEstado() == EstadoAdopcion.EN_PROCESO)
                .filter(a -> a.getGato() != null)
                .toList();
    }

    public List<Gato> listarGatosConAdopcionEnProceso() {
        // usamos las adopciones en proceso para obtener los gatos
        return listarAdopcionesEnProceso().stream()
                .map(Adopcion::getGato)
                .filter(g -> g != null)
                .filter(g -> !Boolean.TRUE.equals(g.isAdoptado())) // 👈 filtro: solo gatos NO adoptados
                .distinct()
                .toList();
    }


}
