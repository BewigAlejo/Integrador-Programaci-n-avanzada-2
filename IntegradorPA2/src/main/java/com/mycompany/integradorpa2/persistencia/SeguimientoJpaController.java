package com.mycompany.integradorpa2.persistencia;

import com.mycompany.integradorpa2.logica.Seguimiento;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SeguimientoJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public SeguimientoJpaController() {
        // usa el mismo PU que el resto de tus controllers
        this.emf = Persistence.createEntityManagerFactory("sistema_gatosPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // --- CRUD ---
    public void create(Seguimiento s) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(s);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public void edit(Seguimiento s) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(s);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public void destroy(Long id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Seguimiento ref = em.find(Seguimiento.class, id);
            if (ref != null) em.remove(ref);
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    // --- queries básicas ---
    public Seguimiento findSeguimiento(Long id) {
        return getEntityManager().find(Seguimiento.class, id);
    }

    public List<Seguimiento> findSeguimientoEntities() {
        return getEntityManager()
                .createQuery("SELECT s FROM Seguimiento s", Seguimiento.class)
                .getResultList();
    }

    public List<Seguimiento> findSeguimientoEntities(int maxResults, int firstResult) {
        return getEntityManager()
                .createQuery("SELECT s FROM Seguimiento s", Seguimiento.class)
                .setMaxResults(maxResults)
                .setFirstResult(firstResult)
                .getResultList();
    }

    public int getSeguimientoCount() {
        Number n = (Number) getEntityManager()
                .createQuery("SELECT COUNT(s) FROM Seguimiento s")
                .getSingleResult();
        return n.intValue();
    }
}
