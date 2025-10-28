package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.Zona;
import com.mycompany.integradorpa2.persistencia.ZonaJpaController;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class ZonaDAOJpa implements ZonaDAO {

    private final ZonaJpaController ctrl = new ZonaJpaController();

    @Override
    public Zona crear(Zona z) {
        ctrl.create(z);
        return z;
    }

    @Override
    public Optional<Zona> buscarPorId(Long id) {
        return Optional.ofNullable(ctrl.findZona(id));
    }

    @Override
    public List<Zona> listarTodos() {
        return ctrl.findZonaEntities();
    }

    @Override
    public Zona actualizar(Zona z) {
        try {
            ctrl.edit(z);
            return z;
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar zona id=" + z.getId(), e);
        }
    }

    @Override
    public void eliminar(Long id) {
        try {
            ctrl.destroy(id);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo eliminar zona id=" + id, e);
        }
    }

    // ---------- Consultas específicas ----------

    @Override
    public Optional<Zona> buscarPorNombreExacto(String nombre) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT z FROM Zona z WHERE z.nombreZona = :n",
                Zona.class);
            q.setParameter("n", nombre);
            var r = q.getResultList();
            return r.isEmpty() ? Optional.empty() : Optional.of(r.get(0));
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<Zona> buscarPorNombreLike(String texto) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT z FROM Zona z " +
                "WHERE LOWER(z.nombreZona) LIKE LOWER(CONCAT('%', :t, '%')) " +
                "ORDER BY z.nombreZona",
                Zona.class);
            q.setParameter("t", texto);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<Zona> listarPorBoundingBox(double latMin, double latMax, double lonMin, double lonMax) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT z FROM Zona z " +
                "WHERE z.coor_lat BETWEEN :latMin AND :latMax " +
                "AND z.coor_lon BETWEEN :lonMin AND :lonMax " +
                "ORDER BY z.coor_lat, z.coor_lon",
                Zona.class);
            q.setParameter("latMin", latMin);
            q.setParameter("latMax", latMax);
            q.setParameter("lonMin", lonMin);
            q.setParameter("lonMax", lonMax);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<Zona> listarConGatos() {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT DISTINCT z FROM Zona z " +
                "LEFT JOIN FETCH z.gatos " +
                "ORDER BY z.id",
                Zona.class);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<Zona> listarConVoluntarios() {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT DISTINCT z FROM Zona z " +
                "LEFT JOIN FETCH z.voluntarios " +
                "ORDER BY z.id",
                Zona.class);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }
}
