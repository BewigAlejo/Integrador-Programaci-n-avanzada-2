package com.mycompany.integradorpa2.persistencia;

import com.mycompany.integradorpa2.logica.Tarea;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

public class TareaJpaController implements Serializable {

    private final EntityManagerFactory emf;

    public TareaJpaController() {
        this.emf = JpaUtil.getEmf(); // Usa el EntityManagerFactory singleton
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // ------------------- CRUD -------------------
    public void create(Tarea t) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(t);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public void edit(Tarea t) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(t);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public void destroy(Long id) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Tarea ref = em.getReference(Tarea.class, id);
            em.remove(ref);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public Tarea findTarea(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarea.class, id);
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<Tarea> findTareaEntities() {
        return findTareaEntities(true, -1, -1);
    }

    public List<Tarea> findTareaEntities(int maxResults, int firstResult) {
        return findTareaEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    private List<Tarea> findTareaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Tarea> cq = em.getCriteriaBuilder().createQuery(Tarea.class);
            Root<Tarea> root = cq.from(Tarea.class);
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
