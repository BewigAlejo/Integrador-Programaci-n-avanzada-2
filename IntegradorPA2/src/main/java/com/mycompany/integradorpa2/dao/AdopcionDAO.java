package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.Adopcion;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AdopcionDAO {
    Adopcion crear(Adopcion a);
    Optional<Adopcion> buscarPorId(Long id);
    List<Adopcion> listarTodos();
    Adopcion actualizar(Adopcion a);
    void eliminar(Long id);

    // Consultas de negocio útiles
    List<Adopcion> listarPorEstado(String estado);         // EN_PROCESO / APROBADA / RECHAZADA, etc.
    List<Adopcion> listarPorFamilia(Integer familiaId);    // si tu Familia usa int id
    List<Adopcion> listarPorGato(Long gatoId);
    List<Adopcion> listarPorFechaEntre(Date desde, Date hasta);
}
