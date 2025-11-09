package com.mycompany.integradorpa2.service;

import com.mycompany.integradorpa2.dao.EntradaHistorialDAO;
import com.mycompany.integradorpa2.dao.EntradaHistorialDAOJpa;
import com.mycompany.integradorpa2.dao.GatoDAO;
import com.mycompany.integradorpa2.dao.GatoDAOJpa;
import com.mycompany.integradorpa2.dao.HistorialMedicoDAO;
import com.mycompany.integradorpa2.dao.HistorialMedicoDAOJpa;
import com.mycompany.integradorpa2.dao.VeterinarioDAO;
import com.mycompany.integradorpa2.dao.VeterinarioDAOJpa;
import com.mycompany.integradorpa2.logica.EntradaHistorial;
import com.mycompany.integradorpa2.logica.Gato;
import com.mycompany.integradorpa2.logica.HistorialMedico;
import com.mycompany.integradorpa2.logica.Veterinario;

import java.time.LocalDate;
import java.util.List;

public class SeguimientoVeterinarioService {

        private final GatoDAO gatoDao = new GatoDAOJpa();
    private final VeterinarioDAO vetDao = new VeterinarioDAOJpa();
    private final HistorialMedicoDAO historialDao = new HistorialMedicoDAOJpa();
    private final EntradaHistorialDAO entradaDao = new EntradaHistorialDAOJpa();

    // =========================================================================
    // 1) Obtener historial médico de un gato (crea si no existe)
    // =========================================================================
    public HistorialMedico obtenerOCrearHistorial(Long gatoId) {
        Gato gato = gatoDao.buscarPorId(gatoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Gato no encontrado con ID: " + gatoId));

        if (gato.getHistorialMedico() != null) {
            return gato.getHistorialMedico();
        }

        HistorialMedico historial = new HistorialMedico();
        historial.setGato(gato);
        historial.setFechaApertura(java.sql.Date.valueOf(java.time.LocalDate.now()));

        HistorialMedico guardado = historialDao.crear(historial);
        gato.setHistorialMedico(guardado);
        gatoDao.actualizar(gato);

        return guardado;
    }

    // =========================================================================
    // 2) Registrar consulta / actualización por veterinario
    // =========================================================================
    public EntradaHistorial registrarEntrada(
            Long gatoId,
            Integer veterinarioId,
            String diagnostico,
            String tratamiento,
            String estudios,
            Boolean aptoAdopcion
    ) {
        if (diagnostico == null || diagnostico.isBlank()) {
            throw new IllegalArgumentException("El diagnóstico es obligatorio.");
        }

        Gato gato = gatoDao.buscarPorId(gatoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Gato no encontrado con ID: " + gatoId));

        Veterinario vet = vetDao.buscarPorId(veterinarioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Veterinario no encontrado con ID: " + veterinarioId));

        // Siempre nos aseguramos de tener historial
        HistorialMedico historial = obtenerOCrearHistorial(gatoId);

        EntradaHistorial entrada = new EntradaHistorial();
        entrada.setHistorial(historial);
        entrada.setVeterinario(vet);
        entrada.setDiagnostico(diagnostico);
        entrada.setTratamiento(tratamiento);
        entrada.setFecha(java.sql.Date.valueOf(java.time.LocalDate.now()));
        // si querés usar estudios / aptoAdopcion, setéalos acá

        EntradaHistorial guardada = entradaDao.crear(entrada);

        // mantener relación en memoria (por si la usás en esta sesión)
        if (historial.getEntradas() != null) {
            historial.getEntradas().add(guardada);
        }

        return guardada;
    }

    // =========================================================================
    // 3) Entradas por gato (SIEMPRE frescas, sin crear historial nuevo)
    // =========================================================================
    public List<EntradaHistorial> listarEntradasPorGato(Long gatoId) {
        return entradaDao.listarTodos().stream()
                .filter(e -> e.getHistorial() != null
                        && e.getHistorial().getGato() != null
                        && gatoId.equals(e.getHistorial().getGato().getId()))
                .toList();
    }

    // =========================================================================
    // 4) Gatos con historial (usando entradas reales)
    // =========================================================================
    public List<Gato> listarGatosConHistorial() {
        // juntamos los IDs de gatos que tienen al menos una entrada
        var idsConEntradas = entradaDao.listarTodos().stream()
                .filter(e -> e.getHistorial() != null && e.getHistorial().getGato() != null)
                .map(e -> e.getHistorial().getGato().getId())
                .collect(java.util.stream.Collectors.toSet());

        // devolvemos sólo esos gatos
        return gatoDao.listarTodos().stream()
                .filter(g -> idsConEntradas.contains(g.getId()))
                .toList();
    }

    // =========================================================================
    // 5) Certificado apto (usa registrarEntrada)
    // =========================================================================
    public EntradaHistorial emitirCertificadoApto(
            Long gatoId,
            Integer veterinarioId,
            String observaciones
    ) {
        String diag = "Evaluación de aptitud para adopción";
        String trat = "Sin restricciones sanitarias relevantes para adopción.";

        return registrarEntrada(
                gatoId,
                veterinarioId,
                diag,
                trat,
                observaciones,
                true
        );
    }
}
