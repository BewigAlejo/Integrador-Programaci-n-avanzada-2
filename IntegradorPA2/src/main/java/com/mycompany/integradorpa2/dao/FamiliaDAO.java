package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.Familia;
import java.util.List;
import java.util.Optional;

public interface FamiliaDAO {
    Familia crear(Familia f);
    Optional<Familia> buscarPorId(Integer id);
    List<Familia> listarTodos();
    Familia actualizar(Familia f);
    void eliminar(Long id);

    // Consultas “de negocio” que te sirven:
    Optional<Familia> buscarPorEmail(String email);
    List<Familia> listarPorReputacionMin(int reputacionMin);
}
