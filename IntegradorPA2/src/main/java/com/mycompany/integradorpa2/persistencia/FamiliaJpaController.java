
package com.mycompany.integradorpa2.persistencia;

import com.mycompany.integradorpa2.logica.Familia;   

import java.util.List;                                
import javax.persistence.EntityManager;               
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;           
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;                  



public class FamiliaJpaController {
    private EntityManagerFactory emf;

    public FamiliaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("sistema_gatosPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    // CREATE
    public void create(Familia familia) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(familia);
            tx.commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    // READ by ID
    public Familia findFamilia(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Familia.class, id);
        } finally {
            em.close();
        }
    }

    // READ all
    public List<Familia> findFamiliaEntities() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Familia> q = em.createQuery(
                "SELECT f FROM Familia f ORDER BY f.id", Familia.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    // READ 
    public List<Familia> findFamiliaEntities(int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Familia> q = em.createQuery(
                "SELECT f FROM Familia f ORDER BY f.id", Familia.class);
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    // BÚSQUEDAS ÚTILES 
    public Familia findByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Familia> q = em.createQuery(
                "SELECT f FROM Familia f WHERE f.email = :email", Familia.class);
            q.setParameter("email", email);
            List<Familia> res = q.getResultList();
            return res.isEmpty() ? null : res.get(0);
        } finally {
            em.close();
        }
    }

    public List<Familia> searchByNombreLike(String patron) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Familia> q = em.createQuery(
                "SELECT f FROM Familia f WHERE LOWER(f.nombre) LIKE LOWER(:p) ORDER BY f.nombre",
                Familia.class);
            q.setParameter("p", "%" + patron + "%");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    // UPDATE
    public void edit(Familia familia) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(familia);
            tx.commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    // DELETE
    public void destroy(int id) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Familia ref = em.find(Familia.class, id);
            if (ref != null) {
                em.remove(ref);
            }
            tx.commit();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    // COUNT 
    public long getFamiliaCount() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(f) FROM Familia f", Long.class)
                     .getSingleResult();
        } finally {
            em.close();
        }
    }  
}
