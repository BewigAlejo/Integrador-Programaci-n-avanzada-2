package com.mycompany.integradorpa2.persistencia;

import com.mycompany.integradorpa2.logica.EntradaHistorial;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

public class EntradaHistorialJpaController implements Serializable {

    private final EntityManagerFactory emf;

    public EntradaHistorialJpaController() {
        this.emf = JpaUtil.getEmf();      
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // ---------------- CRUD ----------------

    public void create(EntradaHistorial e) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(e);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public void edit(EntradaHistorial e) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(e);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public void destroy(Long id) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            EntradaHistorial ref = em.getReference(EntradaHistorial.class, id);
            em.remove(ref);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public EntradaHistorial findEntradaHistorial(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EntradaHistorial.class, id);
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<EntradaHistorial> findEntradaHistorialEntities() {
        return findEntradaHistorialEntities(true, -1, -1);
    }

    public List<EntradaHistorial> findEntradaHistorialEntities(int maxResults, int firstResult) {
        return findEntradaHistorialEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    private List<EntradaHistorial> findEntradaHistorialEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<EntradaHistorial> cq = em.getCriteriaBuilder().createQuery(EntradaHistorial.class);
            Root<EntradaHistorial> root = cq.from(EntradaHistorial.class);
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
