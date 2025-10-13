/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.integradorpa2.persistencia;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Usuario
 */
public class AdopcionJpaController implements Serializable {
    private EntityManagerFactory emf;

    public AdopcionJpaController() {
        this.emf = Persistence.createEntityManagerFactory("sistema_gatosPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
