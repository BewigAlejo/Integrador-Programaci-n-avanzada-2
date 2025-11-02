package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.Veterinario;
import java.util.List;
import java.util.Optional;

public interface VeterinarioDAO {
    Veterinario crear(Veterinario v);
    Optional<Veterinario> buscarPorId(int id);
    List<Veterinario> listarTodos();
    Veterinario actualizar(Veterinario v);
    void eliminar(Integer id);

    // Consultas de negocio útiles
    Optional<Veterinario> buscarPorEmail(String email);
    Optional<Veterinario> buscarPorMatricula(String matricula);
    List<Veterinario> listarPorEspecialidad(String especialidad);
}
