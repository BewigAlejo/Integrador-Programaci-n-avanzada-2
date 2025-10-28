package com.mycompany.integradorpa2.persistencia;

import com.mycompany.integradorpa2.logica.Gato;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

public class GatoJpaController implements Serializable {

    private final EntityManagerFactory emf;

    public GatoJpaController() {
        this.emf = JpaUtil.getEmf(); 
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // ---------- MÉTODOS CRUD ----------

    public void create(Gato gato) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(gato);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public void edit(Gato gato) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(gato);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public void destroy(Long id) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Gato g = em.getReference(Gato.class, id);
            em.remove(g);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public Gato findGato(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Gato.class, id);
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<Gato> findGatoEntities() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Gato> cq = em.getCriteriaBuilder().createQuery(Gato.class);
            cq.select(cq.from(Gato.class));
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}
