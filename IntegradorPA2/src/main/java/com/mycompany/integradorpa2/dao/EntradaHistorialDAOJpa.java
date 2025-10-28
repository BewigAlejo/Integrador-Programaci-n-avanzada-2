package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.EntradaHistorial;
import com.mycompany.integradorpa2.persistencia.EntradaHistorialJpaController;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class EntradaHistorialDAOJpa implements EntradaHistorialDAO {

    private final EntradaHistorialJpaController ctrl = new EntradaHistorialJpaController();

    @Override
    public EntradaHistorial crear(EntradaHistorial e) {
        ctrl.create(e);
        return e;
    }

    @Override
    public Optional<EntradaHistorial> buscarPorId(Long id) {
        return Optional.ofNullable(ctrl.findEntradaHistorial(id));
    }

    @Override
    public List<EntradaHistorial> listarTodos() {
        return ctrl.findEntradaHistorialEntities();
    }

    @Override
    public EntradaHistorial actualizar(EntradaHistorial e) {
        try {
            ctrl.edit(e);
            return e;
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo actualizar entrada id=" + e.getId(), ex);
        }
    }

    @Override
    public void eliminar(Long id) {
        try {
            ctrl.destroy(id);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo eliminar entrada id=" + id, ex);
        }
    }

    // --------------- Consultas específicas ---------------

    @Override
    public List<EntradaHistorial> listarPorHistorial(Long historialId) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT e FROM EntradaHistorial e " +
                "WHERE e.historial.id = :hid ORDER BY e.fecha DESC",
                EntradaHistorial.class);
            q.setParameter("hid", historialId);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<EntradaHistorial> listarPorVeterinario(int veterinarioId) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT e FROM EntradaHistorial e " +
                "WHERE e.veterinario.id = :vid ORDER BY e.fecha DESC",
                EntradaHistorial.class);
            q.setParameter("vid", veterinarioId);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<EntradaHistorial> listarPorFechaEntre(Date desde, Date hasta) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT e FROM EntradaHistorial e " +
                "WHERE e.fecha BETWEEN :d AND :h ORDER BY e.fecha DESC",
                EntradaHistorial.class);
            q.setParameter("d", desde);
            q.setParameter("h", hasta);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<EntradaHistorial> listarUltimas(int n) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT e FROM EntradaHistorial e ORDER BY e.fecha DESC",
                EntradaHistorial.class);
            q.setMaxResults(n);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<EntradaHistorial> buscarPorDiagnostico(String texto) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT e FROM EntradaHistorial e " +
                "WHERE LOWER(e.diagnostico) LIKE LOWER(CONCAT('%', :t, '%')) " +
                "ORDER BY e.fecha DESC",
                EntradaHistorial.class);
            q.setParameter("t", texto);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }
}
