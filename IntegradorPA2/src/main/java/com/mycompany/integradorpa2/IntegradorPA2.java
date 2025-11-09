package com.mycompany.integradorpa2;
import com.mycompany.integradorpa2.consola.AdminMenu;
import com.mycompany.integradorpa2.consola.GatoMenu;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class IntegradorPA2 {

    public static void main(String[] args) {             
        Scanner sc = new Scanner(System.in);
        AdminMenu adminMenu = new AdminMenu(); 
       
        try {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("sistema_gatosPU");
        EntityManager em = emf.createEntityManager();
        em.close();
        System.out.println("✅ Conexión OK y entidades cargadas.");
        } catch (Exception e) {
            System.err.println("❌ Error inicializando JPA: " + e.getMessage());
            e.printStackTrace();
            return; 
        }

        
        int opcion;
        do {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Gestion de Gatos");
            System.out.println("2. Gestion de Adopciones");
            System.out.println("3. (Admin)");
            System.out.println("0. Salir");
            System.out.print("> ");
            opcion = sc.nextInt();
            sc.nextLine(); 

            switch (opcion) {
                case 1 -> new GatoMenu(sc).mostrar();
                case 2 -> System.out.println("→ Menu de adopciones (a implementar)");
                case 3 -> adminMenu.mostrar(sc);
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opcion invalida");
            }

        } while (opcion != 0);

        sc.close();
    }
}
