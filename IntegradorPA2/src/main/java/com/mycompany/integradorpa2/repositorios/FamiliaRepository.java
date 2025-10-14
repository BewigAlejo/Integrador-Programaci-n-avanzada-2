package com.mycompany.integradorpa2.repositorios;

import com.mycompany.integradorpa2.logica.Familia;
import com.mycompany.integradorpa2.persistencia.ControladoraPersistencia;
import java.util.List;

public class FamiliaRepository {

    private final ControladoraPersistencia control;

    public FamiliaRepository(ControladoraPersistencia control) {
        this.control = control;
    }

    public void crear(Familia f) {
        control.familiaJpa.create(f);
    }

    public Familia traerPorId(int id) {
        return control.familiaJpa.findFamilia(id);
    }

    public List<Familia> listar() {
        return control.familiaJpa.findFamiliaEntities();
    }

    public void actualizar(Familia f) throws Exception {
        control.familiaJpa.edit(f);
    }

    public void eliminar(int id) throws Exception {
        control.familiaJpa.destroy(id);
    }
}
