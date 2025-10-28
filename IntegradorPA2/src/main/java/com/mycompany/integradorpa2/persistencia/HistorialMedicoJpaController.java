package com.mycompany.integradorpa2.persistencia;

import com.mycompany.integradorpa2.logica.HistorialMedico;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

public class HistorialMedicoJpaController implements Serializable {

    private final EntityManagerFactory emf;

    public HistorialMedicoJpaController() {
        this.emf = JpaUtil.getEmf();  
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // ---------------- CRUD ----------------

    public void create(HistorialMedico h) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(h);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public void edit(HistorialMedico h) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(h);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public void destroy(Long id) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            HistorialMedico ref = em.getReference(HistorialMedico.class, id);
            em.remove(ref);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public HistorialMedico findHistorialMedico(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HistorialMedico.class, id);
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<HistorialMedico> findHistorialMedicoEntities() {
        return findHistorialMedicoEntities(true, -1, -1);
    }

    public List<HistorialMedico> findHistorialMedicoEntities(int maxResults, int firstResult) {
        return findHistorialMedicoEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    private List<HistorialMedico> findHistorialMedicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<HistorialMedico> cq = em.getCriteriaBuilder().createQuery(HistorialMedico.class);
            Root<HistorialMedico> root = cq.from(HistorialMedico.class);
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
