package com.mycompany.integradorpa2;

import com.mycompany.integradorpa2.persistencia.ControladoraPersistencia;
import com.mycompany.integradorpa2.MenuConsola;

public class IntegradorPA2 {

    public static void main(String[] args) {

        ControladoraPersistencia control = new ControladoraPersistencia();

        
        MenuConsola consola = new MenuConsola(control);

        System.out.println("Tablas creadas correctamente en MySQL.");
        System.out.println("=======================================");
        System.out.println("      SISTEMA DE ADOPCION DE GATOS");
        System.out.println("=======================================");

        
        consola.iniciar();
    }
}
