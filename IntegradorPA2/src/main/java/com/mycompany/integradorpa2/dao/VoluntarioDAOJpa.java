package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.Voluntario;
import com.mycompany.integradorpa2.persistencia.VoluntarioJpaController;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class VoluntarioDAOJpa implements VoluntarioDAO {

    private final VoluntarioJpaController ctrl = new VoluntarioJpaController();

    @Override
    public Voluntario crear(Voluntario v) {
        ctrl.create(v);
        return v;
    }

    @Override
    public Optional<Voluntario> buscarPorId(int id) {
        return Optional.ofNullable(ctrl.findVoluntario(id));
    }

    @Override
    public List<Voluntario> listarTodos() {
        return ctrl.findVoluntarioEntities();
    }

    @Override
    public Voluntario actualizar(Voluntario v) {
        try {
            ctrl.edit(v);
            return v;
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar voluntario id=" + v.getId(), e);
        }
    }

    @Override
    public void eliminar(Integer id) {
        try {
            ctrl.destroy(id);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo eliminar voluntario id=" + id, e);
        }
    }

    // --------- Consultas específicas ---------

    @Override
    public Optional<Voluntario> buscarPorEmail(String email) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT v FROM Voluntario v WHERE v.email = :e", Voluntario.class);
            q.setParameter("e", email);
            var r = q.getResultList();
            return r.isEmpty() ? Optional.empty() : Optional.of(r.get(0));
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<Voluntario> listarPorZonaId(Long zonaId) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT v FROM Voluntario v WHERE v.zona.id = :z ORDER BY v.id",
                Voluntario.class);
            q.setParameter("z", zonaId);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<Voluntario> buscarPorDisponibilidad(String disponibilidad) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT v FROM Voluntario v " +
                "WHERE LOWER(v.disponibilidad) LIKE LOWER(CONCAT('%', :d, '%')) " +
                "ORDER BY v.id",
                Voluntario.class);
            q.setParameter("d", disponibilidad);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }
}
