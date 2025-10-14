package com.mycompany.integradorpa2;

import com.mycompany.integradorpa2.persistencia.ControladoraPersistencia;
import com.mycompany.integradorpa2.repositorios.FamiliaRepository;
import com.mycompany.integradorpa2.CRUD.FamiliaCRUD;
import java.util.Scanner;

public class MenuConsola {

    private final Scanner sc = new Scanner(System.in);
    private final FamiliaCRUD familiaCrud;

    public MenuConsola(ControladoraPersistencia control) {
        var repoFamilia = new FamiliaRepository(control);
        this.familiaCrud = new FamiliaCRUD(repoFamilia);
    }

    public void iniciar() {
        int opcion;
        do {
            System.out.println("=======================================");
            System.out.println("      SISTEMA DE ADOPCION DE GATOS");
            System.out.println("=======================================");
            System.out.println("1. Ingresar como ADMIN");
            System.out.println("2. Ingresar como USUARIO");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");
            opcion = sc.nextInt(); sc.nextLine();

            switch (opcion) {
                case 1 -> menuAdmin();
                case 2 -> menuUsuario();
                case 0 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private void menuAdmin() {
        int opcion;
        do {
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1. CRUD Familia");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opcion: ");
            opcion = sc.nextInt(); sc.nextLine();

            switch (opcion) {
                case 1 -> familiaCrud.mostrarMenu(sc);
                case 0 -> System.out.println("Volviendo al menu principal...");
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    private void menuUsuario() {
        System.out.println("\n=== MENU USUARIO ===");
        System.out.println("(Por el momento no hay opciones disponibles)");
        System.out.println("Presione Enter para volver...");
        sc.nextLine();
    }
}
