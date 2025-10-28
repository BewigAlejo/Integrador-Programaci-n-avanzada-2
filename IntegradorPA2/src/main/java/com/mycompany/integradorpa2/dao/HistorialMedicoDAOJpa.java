package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.HistorialMedico;
import com.mycompany.integradorpa2.persistencia.HistorialMedicoJpaController;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class HistorialMedicoDAOJpa implements HistorialMedicoDAO {

    private final HistorialMedicoJpaController ctrl = new HistorialMedicoJpaController();

    @Override
    public HistorialMedico crear(HistorialMedico h) {
        ctrl.create(h);
        return h;
    }

    @Override
    public Optional<HistorialMedico> buscarPorId(Long id) {
        return Optional.ofNullable(ctrl.findHistorialMedico(id));
    }

    @Override
    public List<HistorialMedico> listarTodos() {
        return ctrl.findHistorialMedicoEntities();
    }

    @Override
    public HistorialMedico actualizar(HistorialMedico h) {
        try {
            ctrl.edit(h);
            return h;
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar historial id=" + h.getId(), e);
        }
    }

    @Override
    public void eliminar(Long id) {
        try {
            ctrl.destroy(id);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo eliminar historial id=" + id, e);
        }
    }

    // --------------- Consultas específicas ---------------

    @Override
    public Optional<HistorialMedico> buscarPorGatoId(Long gatoId) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT h FROM HistorialMedico h WHERE h.gato.id = :gid",
                HistorialMedico.class);
            q.setParameter("gid", gatoId);
            var r = q.getResultList();
            return r.isEmpty() ? Optional.empty() : Optional.of(r.get(0));
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public boolean existePorGatoId(Long gatoId) {
        EntityManager em = ctrl.getEntityManager();
        try {
            Long count = em.createQuery(
                "SELECT COUNT(h) FROM HistorialMedico h WHERE h.gato.id = :gid", Long.class)
                .setParameter("gid", gatoId)
                .getSingleResult();
            return count != null && count > 0;
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<HistorialMedico> listarPorFechaAperturaEntre(Date desde, Date hasta) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT h FROM HistorialMedico h " +
                "WHERE h.fechaApertura BETWEEN :d AND :h " +
                "ORDER BY h.fechaApertura DESC",
                HistorialMedico.class);
            q.setParameter("d", desde);
            q.setParameter("h", hasta);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }
}
