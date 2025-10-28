package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.Voluntario;
import java.util.List;
import java.util.Optional;

public interface VoluntarioDAO {
    Voluntario crear(Voluntario v);
    Optional<Voluntario> buscarPorId(int id);
    List<Voluntario> listarTodos();
    Voluntario actualizar(Voluntario v);
    void eliminar(int id);

    // Consultas de negocio (opcionales pero útiles)
    Optional<Voluntario> buscarPorEmail(String email);
    List<Voluntario> listarPorZonaId(Long zonaId);
    List<Voluntario> buscarPorDisponibilidad(String disponibilidad); // exacta o “AM/PM”, etc.
}
