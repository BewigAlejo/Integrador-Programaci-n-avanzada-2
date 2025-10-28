package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.Gato;
import com.mycompany.integradorpa2.persistencia.GatoJpaController;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class GatoDAOJpa implements GatoDAO {

    private final GatoJpaController ctrl = new GatoJpaController();

    @Override
    public Gato crear(Gato g) {
        ctrl.create(g);   // asigna ID si es autogenerado
        return g;
    }

    @Override
    public Optional<Gato> buscarPorId(Long id) {
        return Optional.ofNullable(ctrl.findGato(id));
    }

    @Override
    public List<Gato> listarTodos() {
        return ctrl.findGatoEntities();
    }

    @Override
    public Gato actualizar(Gato g) {
        try {
            ctrl.edit(g);
            return g;
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar gato id=" + g.getId(), e);
        }
    }

    @Override
    public void eliminar(Long id) {
        try {
            ctrl.destroy(id);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo eliminar gato id=" + id, e);
        }
    }

    // ---------------- Consultas específicas ----------------

    @Override
    public List<Gato> buscarPorColor(String color) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT g FROM Gato g WHERE g.color = :c ORDER BY g.id",
                Gato.class
            );
            q.setParameter("c", color);
            return q.getResultList();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    @Override
    public List<Gato> buscarPorZonaId(Long zonaId) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT g FROM Gato g WHERE g.zona.id = :z ORDER BY g.id",
                Gato.class
            );
            q.setParameter("z", zonaId);
            return q.getResultList();
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}
