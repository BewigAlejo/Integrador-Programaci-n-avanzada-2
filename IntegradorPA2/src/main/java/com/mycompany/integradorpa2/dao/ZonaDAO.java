package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.Zona;

import java.util.List;
import java.util.Optional;

public interface ZonaDAO {
    Zona crear(Zona z);
    Optional<Zona> buscarPorId(Long id);
    List<Zona> listarTodos();
    Zona actualizar(Zona z);
    void eliminar(Long id);

    // Consultas útiles
    Optional<Zona> buscarPorNombreExacto(String nombre);
    List<Zona> buscarPorNombreLike(String texto);
    List<Zona> listarPorBoundingBox(double latMin, double latMax, double lonMin, double lonMax);
    List<Zona> listarConGatos();        // carga las relaciones (join fetch)
    List<Zona> listarConVoluntarios();  // idem
}
