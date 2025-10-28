package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.Tarea;
import com.mycompany.integradorpa2.persistencia.TareaJpaController;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TareaDAOJpa implements TareaDAO {

    private final TareaJpaController ctrl = new TareaJpaController();

    @Override
    public Tarea crear(Tarea t) {
        ctrl.create(t);
        return t;
    }

    @Override
    public Optional<Tarea> buscarPorId(Long id) {
        return Optional.ofNullable(ctrl.findTarea(id));
    }

    @Override
    public List<Tarea> listarTodas() {
        return ctrl.findTareaEntities();
    }

    @Override
    public Tarea actualizar(Tarea t) {
        try {
            ctrl.edit(t);
            return t;
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar tarea id=" + t.getId(), e);
        }
    }

    @Override
    public void eliminar(Long id) {
        try {
            ctrl.destroy(id);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo eliminar tarea id=" + id, e);
        }
    }

    // ---------------- Consultas específicas ----------------

    @Override
    public List<Tarea> listarPorEstado(String estado) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT t FROM Tarea t WHERE t.estadoTarea = :e ORDER BY t.fecha DESC",
                Tarea.class);
            q.setParameter("e", estado);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<Tarea> listarPorTipo(String tipo) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT t FROM Tarea t WHERE t.tipoTarea = :t ORDER BY t.fecha DESC",
                Tarea.class);
            q.setParameter("t", tipo);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<Tarea> listarPorVoluntario(int voluntarioId) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT t FROM Tarea t WHERE t.asignadaA.id = :v ORDER BY t.fecha DESC",
                Tarea.class);
            q.setParameter("v", voluntarioId);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<Tarea> listarPorGato(Long gatoId) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT t FROM Tarea t WHERE t.gato.id = :g ORDER BY t.fecha DESC",
                Tarea.class);
            q.setParameter("g", gatoId);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<Tarea> listarEntreFechas(Date desde, Date hasta) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT t FROM Tarea t WHERE t.fecha BETWEEN :d AND :h ORDER BY t.fecha DESC",
                Tarea.class);
            q.setParameter("d", desde);
            q.setParameter("h", hasta);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<Tarea> listarPendientes() {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT t FROM Tarea t WHERE t.estadoTarea = 'PENDIENTE' ORDER BY t.fecha ASC",
                Tarea.class);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public long contarPorEstado(String estado) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT COUNT(t) FROM Tarea t WHERE t.estadoTarea = :e",
                Long.class);
            q.setParameter("e", estado);
            return q.getSingleResult();
        } finally { if (em.isOpen()) em.close(); }
    }
}
