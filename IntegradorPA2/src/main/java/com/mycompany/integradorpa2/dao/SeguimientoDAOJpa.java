package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.Seguimiento;
import com.mycompany.integradorpa2.persistencia.SeguimientoJpaController;
import java.util.List;
import java.util.Optional;

public class SeguimientoDAOJpa implements SeguimientoDAO {

    private final SeguimientoJpaController ctrl = new SeguimientoJpaController();

    @Override
    public Seguimiento crear(Seguimiento s) {
        ctrl.create(s);
        return s;
    }

    @Override
    public Optional<Seguimiento> buscarPorId(Long id) {
        return Optional.ofNullable(ctrl.findSeguimiento(id));
    }

    @Override
    public List<Seguimiento> listarTodos() {
        return ctrl.findSeguimientoEntities();
    }

    @Override
    public Seguimiento actualizar(Seguimiento s) {
        try {
            ctrl.edit(s);
            return s;
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar el seguimiento ID=" + s.getId(), e);
        }
    }

    @Override
    public void eliminar(Long id) {
        ctrl.destroy(id);
    }
}
