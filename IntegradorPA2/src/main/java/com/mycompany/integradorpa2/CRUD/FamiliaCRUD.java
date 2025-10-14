package com.mycompany.integradorpa2.CRUD;

import com.mycompany.integradorpa2.logica.Familia;
import com.mycompany.integradorpa2.repositorios.FamiliaRepository;
import java.util.List;
import java.util.Scanner;

public class FamiliaCRUD {

    private final FamiliaRepository repo;

    public FamiliaCRUD(FamiliaRepository repo) {
        this.repo = repo;
    }

    public void mostrarMenu(Scanner sc) {
        int opcion;
        do {
            System.out.println("\n=== CRUD FAMILIA ===");
            System.out.println("1. Crear familia");
            System.out.println("2. Listar familias");
            System.out.println("3. Editar familia");
            System.out.println("4. Eliminar familia");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opcion: ");
            opcion = sc.nextInt(); sc.nextLine();

            switch (opcion) {
                case 1 -> crear(sc);
                case 2 -> listar();
                case 3 -> editar(sc);
                case 4 -> eliminar(sc);
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    public void crear(Scanner sc) {
        Familia f = new Familia();
        System.out.print("Nombre: ");       f.setNombre(sc.nextLine());
        System.out.print("Email: ");        f.setEmail(sc.nextLine());
        System.out.print("Telefono: ");     f.setTelefono(sc.nextLine());
        f.setRol("FAMILIA");
        System.out.print("Direccion: ");    f.setDireccion(sc.nextLine());
        System.out.print("Coordenadas: ");  f.setCoordenadas(sc.nextLine());
        System.out.print("Reputacion (1-10): ");
        f.setReputacion(sc.nextInt()); sc.nextLine();

        repo.crear(f);
        System.out.println("Familia creada correctamente. ID: " + f.getId());
    }

    public void listar() {
        List<Familia> familias = repo.listar();
        System.out.println("\nLISTADO DE FAMILIAS:");
        if (familias.isEmpty()) {
            System.out.println("(No hay familias registradas)");
            return;
        }
        for (Familia f : familias) {
            System.out.println("ID: " + f.getId()
                    + " | Nombre: " + f.getNombre()
                    + " | Email: " + f.getEmail()
                    + " | Reputacion: " + f.getReputacion());
        }
    }

    public void editar(Scanner sc) {
        listar();
        System.out.print("\nID de la familia a editar: ");
        int id = sc.nextInt(); sc.nextLine();

        Familia f = repo.traerPorId(id);
        if (f == null) {
            System.out.println("No se encontro la familia con ID " + id);
            return;
        }

        System.out.print("Nuevo nombre (" + f.getNombre() + "): ");
        String nuevoNombre = sc.nextLine();
        if (!nuevoNombre.isEmpty()) f.setNombre(nuevoNombre);

        System.out.print("Nueva reputacion (" + f.getReputacion() + "): ");
        String repStr = sc.nextLine();
        if (!repStr.isEmpty()) f.setReputacion(Integer.parseInt(repStr));

        try {
            repo.actualizar(f);
            System.out.println("Familia actualizada correctamente.");
        } catch (Exception e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
    }

    public void eliminar(Scanner sc) {
        listar();
        System.out.print("\nID de la familia a eliminar: ");
        int id = sc.nextInt(); sc.nextLine();

        try {
            repo.eliminar(id);
            System.out.println("Familia eliminada correctamente.");
        } catch (Exception e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }
}
