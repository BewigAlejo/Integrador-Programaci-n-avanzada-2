package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.EntradaHistorial;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EntradaHistorialDAO {
    EntradaHistorial crear(EntradaHistorial e);
    Optional<EntradaHistorial> buscarPorId(Long id);
    List<EntradaHistorial> listarTodos();
    EntradaHistorial actualizar(EntradaHistorial e);
    void eliminar(Long id);

    // Consultas de negocio útiles
    List<EntradaHistorial> listarPorHistorial(Long historialId);
    List<EntradaHistorial> listarPorVeterinario(int veterinarioId);
    List<EntradaHistorial> listarPorFechaEntre(Date desde, Date hasta);
    List<EntradaHistorial> listarUltimas(int n);                 // últimas N por fecha desc
    List<EntradaHistorial> buscarPorDiagnostico(String texto);   // like
}
