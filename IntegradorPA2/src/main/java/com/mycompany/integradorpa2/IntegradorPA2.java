/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.integradorpa2;

import com.mycompany.integradorpa2.persistencia.ControladoraPersistencia;
import javax.persistence.Persistence;

/**
 *
 * @author Usuario
 */
public class IntegradorPA2 {

    public static void main(String[] args) {
        new ControladoraPersistencia();
        System.out.println("✅ Tablas creadas correctamente en MySQL.");
    }
}
