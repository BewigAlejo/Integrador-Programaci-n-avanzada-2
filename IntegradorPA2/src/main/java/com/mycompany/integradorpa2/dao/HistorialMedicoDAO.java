package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.HistorialMedico;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface HistorialMedicoDAO {
    HistorialMedico crear(HistorialMedico h);
    Optional<HistorialMedico> buscarPorId(Long id);
    List<HistorialMedico> listarTodos();
    HistorialMedico actualizar(HistorialMedico h);
    void eliminar(Long id);

    // Consultas de negocio
    Optional<HistorialMedico> buscarPorGatoId(Long gatoId);
    boolean existePorGatoId(Long gatoId);
    List<HistorialMedico> listarPorFechaAperturaEntre(Date desde, Date hasta);
}
