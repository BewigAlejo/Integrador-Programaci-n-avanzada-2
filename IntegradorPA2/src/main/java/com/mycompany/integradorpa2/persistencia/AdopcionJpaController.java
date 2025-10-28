package com.mycompany.integradorpa2.persistencia;

import com.mycompany.integradorpa2.logica.Adopcion;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

public class AdopcionJpaController implements Serializable {

    private final EntityManagerFactory emf;

    public AdopcionJpaController() {
        this.emf = JpaUtil.getEmf();          
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // ---------------- CRUD ----------------

    public void create(Adopcion a) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(a);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public void edit(Adopcion a) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(a);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public void destroy(Long id) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Adopcion ref = em.getReference(Adopcion.class, id);
            em.remove(ref);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public Adopcion findAdopcion(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Adopcion.class, id);
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<Adopcion> findAdopcionEntities() {
        return findAdopcionEntities(true, -1, -1);
    }

    public List<Adopcion> findAdopcionEntities(int maxResults, int firstResult) {
        return findAdopcionEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    private List<Adopcion> findAdopcionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Adopcion> cq = em.getCriteriaBuilder().createQuery(Adopcion.class);
            Root<Adopcion> root = cq.from(Adopcion.class);
            cq.select(root);
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}
