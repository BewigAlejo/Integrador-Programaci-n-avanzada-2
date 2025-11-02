package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.Veterinario;
import com.mycompany.integradorpa2.persistencia.VeterinarioJpaController;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class VeterinarioDAOJpa implements VeterinarioDAO {

    private final VeterinarioJpaController ctrl = new VeterinarioJpaController();

    @Override
    public Veterinario crear(Veterinario v) {
        ctrl.create(v);
        return v;
    }

    @Override
    public Optional<Veterinario> buscarPorId(int id) {
        return Optional.ofNullable(ctrl.findVeterinario(id));
    }

    @Override
    public List<Veterinario> listarTodos() {
        return ctrl.findVeterinarioEntities();
    }

    @Override
    public Veterinario actualizar(Veterinario v) {
        try {
            ctrl.edit(v);
            return v;
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar veterinario id=" + v.getId(), e);
        }
    }

    @Override
    public void eliminar(Integer id) {
        try {
            ctrl.destroy(id);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo eliminar veterinario id=" + id, e);
        }
    }

    // ------------- Consultas específicas -------------

    @Override
    public Optional<Veterinario> buscarPorEmail(String email) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT v FROM Veterinario v WHERE v.email = :e", Veterinario.class);
            q.setParameter("e", email);
            var r = q.getResultList();
            return r.isEmpty() ? Optional.empty() : Optional.of(r.get(0));
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public Optional<Veterinario> buscarPorMatricula(String matricula) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT v FROM Veterinario v WHERE v.matricula = :m", Veterinario.class);
            q.setParameter("m", matricula);
            var r = q.getResultList();
            return r.isEmpty() ? Optional.empty() : Optional.of(r.get(0));
        } finally { if (em.isOpen()) em.close(); }
    }

    @Override
    public List<Veterinario> listarPorEspecialidad(String especialidad) {
        EntityManager em = ctrl.getEntityManager();
        try {
            var q = em.createQuery(
                "SELECT v FROM Veterinario v " +
                "WHERE LOWER(v.especialidad) LIKE LOWER(CONCAT('%', :esp, '%')) " +
                "ORDER BY v.id",
                Veterinario.class);
            q.setParameter("esp", especialidad);
            return q.getResultList();
        } finally { if (em.isOpen()) em.close(); }
    }
}
