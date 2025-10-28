package com.mycompany.integradorpa2;

import com.mycompany.integradorpa2.dao.FamiliaDAOJpa;
import com.mycompany.integradorpa2.logica.Familia;
import java.util.Scanner;

public class MenuConsola {
    private final FamiliaDAOJpa familiaDao = new FamiliaDAOJpa();

    public void iniciar() {
        Scanner sc = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Familias");
            System.out.println("2. Gatos");
            System.out.println("3. Tareas");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> menuFamilias(sc);
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void menuFamilias(Scanner sc) {
        int op;
        do {
            System.out.println("\n=== CRUD FAMILIAS ===");
            System.out.println("1. Crear");
            System.out.println("2. Listar");
            System.out.println("3. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            op = sc.nextInt(); sc.nextLine();

            switch (op) {
                case 1 -> {
                    Familia f = new Familia();
                    System.out.print("Nombre: "); f.setNombre(sc.nextLine());
                    System.out.print("Email: "); f.setEmail(sc.nextLine());
                    familiaDao.crear(f);
                }
                case 2 -> familiaDao.listarTodos().forEach(System.out::println);
                case 3 -> {
                    System.out.print("ID a eliminar: ");
                    Long id = sc.nextLong();
                    familiaDao.eliminar(id);
                }
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 0);
    }
}
