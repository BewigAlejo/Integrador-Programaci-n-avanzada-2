package com.mycompany.integradorpa2.persistencia;

import com.mycompany.integradorpa2.logica.Zona;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

public class ZonaJpaController implements Serializable {

    private final EntityManagerFactory emf;

    public ZonaJpaController() {
        this.emf = JpaUtil.getEmf(); 
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // ------------- CRUD -------------
    public void create(Zona z) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(z);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public void edit(Zona z) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(z);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public void destroy(Long id) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Zona ref = em.getReference(Zona.class, id);
            em.remove(ref);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public Zona findZona(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Zona.class, id);
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<Zona> findZonaEntities() {
        return findZonaEntities(true, -1, -1);
    }

    public List<Zona> findZonaEntities(int maxResults, int firstResult) {
        return findZonaEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    private List<Zona> findZonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Zona> cq = em.getCriteriaBuilder().createQuery(Zona.class);
            Root<Zona> root = cq.from(Zona.class);
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
