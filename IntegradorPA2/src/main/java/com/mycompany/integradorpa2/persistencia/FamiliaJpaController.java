package com.mycompany.integradorpa2.persistencia;

import com.mycompany.integradorpa2.logica.Adopcion;
import com.mycompany.integradorpa2.logica.Familia;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FamiliaJpaController implements Serializable {

    private final EntityManagerFactory emf;

    public FamiliaJpaController() {
        this.emf = JpaUtil.getEmf();  
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // ---------- CRUD ----------
    public void create(Familia familia) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            // evitar NPE y asegurar backrefs
            if (familia.getAdopciones() == null) {
                familia.setAdopciones(new ArrayList<>());
            }   

            em.persist(familia);              // INSERT
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public void edit(Familia familia) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(familia);               // UPDATE por ID
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public void destroy(Integer id) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            // getReference es más liviano si sólo vamos a borrar
            Familia f = em.getReference(Familia.class, id);
            // Si querés borrar también adopciones huérfanas, considerá orphanRemoval=true en la entidad
            em.remove(f);                    // DELETE
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public Familia findFamilia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Familia.class, id);   // SELECT por ID
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<Familia> findFamiliaEntities() {
        return findFamiliaEntities(true, -1, -1);
    }

    public List<Familia> findFamiliaEntities(int maxResults, int firstResult) {
        return findFamiliaEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    private List<Familia> findFamiliaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Familia> cq = em.getCriteriaBuilder().createQuery(Familia.class);
            Root<Familia> root = cq.from(Familia.class);
            cq.select(root);
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();            // SELECT *
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public int getFamiliaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Long> cq = em.getCriteriaBuilder().createQuery(Long.class);
            Root<Familia> rt = cq.from(Familia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}
