package com.mycompany.integradorpa2.service;

import com.mycompany.integradorpa2.dao.*;
import com.mycompany.integradorpa2.logica.*;
import com.mycompany.integradorpa2.logica.enums.EstadoTarea;
import com.mycompany.integradorpa2.logica.enums.TipoTarea;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class TareaService {

    private final TareaDAO tareaDao = new TareaDAOJpa();
    private final VoluntarioDAO voluntarioDao = new VoluntarioDAOJpa();
    private final GatoDAO gatoDao = new GatoDAOJpa();
    private final ZonaDAO zonaDao = new ZonaDAOJpa();

    public Tarea registrarTareaRealizada(
            Integer voluntarioId,
            TipoTarea tipo,
            String descripcion,
            String observacion,
            LocalDateTime fechaHora,
            Long gatoId,
            Long zonaId
    ) {
        Voluntario v = voluntarioDao.buscarPorId(voluntarioId)
                .orElseThrow(() -> new IllegalArgumentException("Voluntario no encontrado: " + voluntarioId));

        // Gato opcional
        Gato gato = null;
        if (gatoId != null) {
            gato = gatoDao.buscarPorId(gatoId)
                    .orElseThrow(() -> new IllegalArgumentException("Gato no encontrado: " + gatoId));
        }

        // Fecha/hora
        Date fecha = Date.from(
                (fechaHora != null ? fechaHora : LocalDateTime.now())
                        .atZone(ZoneId.systemDefault()).toInstant()
        );

        // Construcción de la Tarea
        Tarea t = new Tarea();
        t.setAsignadaA(v);                     // ManyToOne Voluntario
        t.setTipoTarea(tipo);                  // enum
        t.setEstadoTarea(EstadoTarea.HECHA);   // como es “realizada”, va directa a HECHA (ajustable)
        t.setDescripcion(descripcion);
        t.setObservacion(observacion);
        t.setFecha(fecha);
        t.setGato(gato);

        // ===== Ubicación / Zona =====
        // Si tu entidad Tarea YA tiene relación con Zona (p.ej. `@ManyToOne Zona zona;`)
        // descomenta la asignación. Si NO la tiene, mira la nota al final.
        Zona zona = resolverZonaParaTarea(zonaId, v, gato);
        try {
            // t.setZona(zona);   // <-- usa esto si tu Tarea tiene el campo `Zona zona`
        } catch (Exception ignored) {
            // Si no existe el campo 'zona' en Tarea, lo ignoramos sin romper el flujo.
        }

        return tareaDao.crear(t);
    }

    
    public Tarea cambiarEstado(Long tareaId, EstadoTarea nuevoEstado) {
        Tarea t = tareaDao.buscarPorId(tareaId)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada: " + tareaId));
        t.setEstadoTarea(nuevoEstado);
        return tareaDao.actualizar(t);
    }

    
    public Tarea reasignarVoluntario(Long tareaId, Integer nuevoVoluntarioId) {
        Tarea t = tareaDao.buscarPorId(tareaId)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada: " + tareaId));

        Voluntario v = voluntarioDao.buscarPorId(nuevoVoluntarioId)
                .orElseThrow(() -> new IllegalArgumentException("Voluntario no encontrado: " + nuevoVoluntarioId));

        t.setAsignadaA(v);
        return tareaDao.actualizar(t);
    }

    
    public List<Tarea> listarTodas() {
        return tareaDao.listarTodas();
    }

    
    public List<Tarea> listarPorVoluntario(Integer voluntarioId) {
        return tareaDao.listarTodas().stream()
                .filter(t -> t.getAsignadaA() != null && Objects.equals(t.getAsignadaA().getId(), voluntarioId))
                .collect(Collectors.toList());
    }

    
    public List<Tarea> listarPorFecha(LocalDate desde, LocalDate hasta) {
        Date dDesde = desde == null ? new Date(0) :
                Date.from(desde.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dHasta = hasta == null ?
                Date.from(LocalDate.now().plusYears(20).atStartOfDay(ZoneId.systemDefault()).toInstant()) :
                Date.from(hasta.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        return tareaDao.listarTodas().stream()
                .filter(t -> t.getFecha() != null &&
                        !t.getFecha().before(dDesde) && t.getFecha().before(dHasta))
                .collect(Collectors.toList());
    }

    
    public void eliminar(Long tareaId) {
        tareaDao.eliminar(tareaId);
    }

    // ================== Helpers ==================

    private Zona resolverZonaParaTarea(Long zonaId, Voluntario voluntario, Gato gato) {
        // 1) Zona explícita
        if (zonaId != null) {
            return zonaDao.buscarPorId(zonaId)
                    .orElseThrow(() -> new IllegalArgumentException("Zona no encontrada: " + zonaId));
        }
        // 2) Zona asignada al voluntario (si tu modelo la tiene)
        try {
            Zona zv = (Zona) Voluntario.class.getMethod("getZonaAsignada").invoke(voluntario);
            if (zv != null) return zv;
        } catch (Exception ignored) {}

        // 3) Zona del gato (si está disponible)
        if (gato != null && gato.getZona() != null) return gato.getZona();

        // 4) Sin zona (permite registrar igual)
        return null;
    }
}
