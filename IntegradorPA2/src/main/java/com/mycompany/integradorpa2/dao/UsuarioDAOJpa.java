package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.Usuario;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class UsuarioDAOJpa implements UsuarioDAO {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("IntegradorPA2PU");

    private EntityManager em() { return emf.createEntityManager(); }

   
    public Usuario crear(Usuario u) {
        EntityManager em = em();
        try {
            em.getTransaction().begin();
            em.persist(u);
            em.getTransaction().commit();
            return u;
        } finally {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            em.close();
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) {
        EntityManager em = em();
        try {
            return Optional.ofNullable(em.find(Usuario.class, id));
        } finally { em.close(); }
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        EntityManager em = em();
        try {
            List<Usuario> res = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                .setParameter("email", email)
                .setMaxResults(1)
                .getResultList();
            return res.isEmpty() ? Optional.empty() : Optional.of(res.get(0));
        } finally { em.close(); }
    }

    @Override
    public List<Usuario> listarTodos() {
        EntityManager em = em();
        try {
            return em.createQuery("SELECT u FROM Usuario u", Usuario.class)
                     .getResultList();
        } finally { em.close(); }
    }

    // Si en tu entidad Usuario tenés un campo enum/String llamado "rol"
    @Override
    public List<? extends Usuario> listarFamilias() {
        EntityManager em = em();
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.rol = :r", Usuario.class)
                     .setParameter("r", "FAMILIA") // o Rol.FAMILIA si es enum
                     .getResultList();
        } finally { em.close(); }
    }

    @Override
    public List<? extends Usuario> listarVoluntarios() {
        EntityManager em = em();
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.rol = :r", Usuario.class)
                     .setParameter("r", "VOLUNTARIO")
                     .getResultList();
        } finally { em.close(); }
    }

    @Override
    public List<? extends Usuario> listarVeterinarios() {
        EntityManager em = em();
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.rol = :r", Usuario.class)
                     .setParameter("r", "VETERINARIO")
                     .getResultList();
        } finally { em.close(); }
    }
}

