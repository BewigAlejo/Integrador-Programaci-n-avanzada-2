package com.mycompany.integradorpa2;
import com.mycompanny.integradorpa2.consola.AdminMenu;
import java.util.Scanner;

public class IntegradorPA2 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        AdminMenu adminMenu = new AdminMenu(); 

        int opcion;
        do {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Gestion de Gatos");
            System.out.println("2. Gestion de Zonas");
            System.out.println("3. Gestion de Adopciones");
            System.out.println("4. Administracion de Usuarios (Admin)");
            System.out.println("0. Salir");
            System.out.print("> ");
            opcion = sc.nextInt();
            sc.nextLine(); 

            switch (opcion) {
                case 1 -> System.out.println("→ Menu de gatos (a implementar)");
                case 2 -> System.out.println("→ Menu de zonas (a implementar)");
                case 3 -> System.out.println("→ Menu de adopciones (a implementar)");
                case 4 -> adminMenu.mostrar(sc);
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opcion invalida");
            }

        } while (opcion != 0);

        sc.close();
    }
}
