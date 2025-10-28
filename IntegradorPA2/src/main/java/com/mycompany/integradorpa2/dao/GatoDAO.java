package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.Gato;
import java.util.List;
import java.util.Optional;

public interface GatoDAO {
    Gato crear(Gato g);
    Optional<Gato> buscarPorId(Long id);
    List<Gato> listarTodos();
    Gato actualizar(Gato g);
    void eliminar(Long id);

    // Consultas útiles de dominio (opcionales)
    List<Gato> buscarPorColor(String color);
    List<Gato> buscarPorZonaId(Long zonaId);
}
