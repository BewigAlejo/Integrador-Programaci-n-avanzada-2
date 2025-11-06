package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.Seguimiento;
import java.util.List;
import java.util.Optional;

public interface SeguimientoDAO {
    Seguimiento crear(Seguimiento s);
    Optional<Seguimiento> buscarPorId(Long id);
    List<Seguimiento> listarTodos();
    Seguimiento actualizar(Seguimiento s);
    void eliminar(Long id);
}
