package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.Familia;
import com.mycompany.integradorpa2.logica.Veterinario;
import com.mycompany.integradorpa2.persistencia.FamiliaJpaController;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;

public class FamiliaDAOJpa implements FamiliaDAO {

    private final FamiliaJpaController ctrl = new FamiliaJpaController();

    @Override
    public Familia crear(Familia f) {
        ctrl.create(f);
        return f;
    }

    @Override
    public Optional<Familia> buscarPorId(Integer id) {
        return Optional.ofNullable(ctrl.findFamilia(id));
    }

    @Override
    public List<Familia> listarTodos() {
        return ctrl.findFamiliaEntities();
    }

    @Override
    public Familia actualizar(Familia f) {
        try {
            ctrl.edit(f);
            return f;
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar familia id=" + f.getId(), e);
        }
    }

    @Override
    public void eliminar(Integer id) {
        try {
            ctrl.destroy(id);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo eliminar familia id=" + id, e);
        }
    }

    // -------- consultas específicas --------

    @Override
    public Optional<Familia> buscarPorEmail(String email) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT f FROM Familia f WHERE f.email = :email", Familia.class);
            q.setParameter("email", email);
            List<Familia> r = q.getResultList();
            return r.isEmpty() ? Optional.empty() : Optional.of(r.get(0));
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    @Override
    public List<Familia> listarPorReputacionMin(int reputacionMin) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT f FROM Familia f WHERE f.reputacion >= :rep ORDER BY f.reputacion DESC",
                Familia.class);
            q.setParameter("rep", reputacionMin);
            return q.getResultList();
        } finally {
            if (em.isOpen()) em.close();
        }
    }
    
    public Optional<Familia> buscarPorUsuarioYPassword(String usuario, String pass) {
        EntityManager em = ctrl.getEntityManager();
        try {
            Familia f = em.createQuery(
                    "SELECT f FROM Familia f WHERE f.usuario = :u AND f.contrasenia = :p",
                    Familia.class)
                .setParameter("u", usuario)
                .setParameter("p", pass)
                .getSingleResult();
            return Optional.of(f);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
