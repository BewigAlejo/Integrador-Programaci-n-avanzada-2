package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.Tarea;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TareaDAO {
    Tarea crear(Tarea t);
    Optional<Tarea> buscarPorId(Long id);
    List<Tarea> listarTodas();
    Tarea actualizar(Tarea t);
    void eliminar(Long id);

    // Consultas específicas
    List<Tarea> listarPorEstado(String estado);
    List<Tarea> listarPorTipo(String tipo);
    List<Tarea> listarPorVoluntario(int voluntarioId);
    List<Tarea> listarPorGato(Long gatoId);
    List<Tarea> listarEntreFechas(Date desde, Date hasta);
    List<Tarea> listarPendientes();
    long contarPorEstado(String estado);
}
