package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.Adopcion;
import com.mycompany.integradorpa2.persistencia.AdopcionJpaController;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AdopcionDAOJpa implements AdopcionDAO {

    private final AdopcionJpaController ctrl = new AdopcionJpaController();

    @Override
    public Adopcion crear(Adopcion a) {
        ctrl.create(a);
        return a;
    }

    @Override
    public Optional<Adopcion> buscarPorId(Long id) {
        return Optional.ofNullable(ctrl.findAdopcion(id));
    }

    @Override
    public List<Adopcion> listarTodos() {
        return ctrl.findAdopcionEntities();
    }

    @Override
    public Adopcion actualizar(Adopcion a) {
        try {
            ctrl.edit(a);
            return a;
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar adopción id=" + a.getId(), e);
        }
    }

    @Override
    public void eliminar(Long id) {
        try {
            ctrl.destroy(id);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo eliminar adopción id=" + id, e);
        }
    }

    // --------------- Consultas específicas ---------------

    @Override
    public List<Adopcion> listarPorEstado(String estado) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT a FROM Adopcion a WHERE a.estado = :est ORDER BY a.fechaAdopcion DESC",
                Adopcion.class);
            q.setParameter("est", estado);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<Adopcion> listarPorFamilia(Integer familiaId) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT a FROM Adopcion a WHERE a.familia.id = :fid ORDER BY a.fechaAdopcion DESC",
                Adopcion.class);
            q.setParameter("fid", familiaId);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<Adopcion> listarPorGato(Long gatoId) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT a FROM Adopcion a WHERE a.gato.id = :gid ORDER BY a.fechaAdopcion DESC",
                Adopcion.class);
            q.setParameter("gid", gatoId);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<Adopcion> listarPorFechaEntre(Date desde, Date hasta) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT a FROM Adopcion a " +
                "WHERE a.fechaAdopcion BETWEEN :d AND :h " +
                "ORDER BY a.fechaAdopcion DESC",
                Adopcion.class);
            q.setParameter("d", desde);
            q.setParameter("h", hasta);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }
}
