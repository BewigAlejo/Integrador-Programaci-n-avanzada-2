package com.mycompany.integradorpa2.consola;
import com.mycompany.integradorpa2.logica.Gato;
import com.mycompany.integradorpa2.logica.enums.EstadoSalud;
import com.mycompany.integradorpa2.service.GatoService;

import java.util.List;
import java.util.Scanner;

public class GatoMenu {

    private final Scanner sc;
    private final GatoService service = new GatoService();

    public GatoMenu(Scanner sc) {
        this.sc = sc;
    }

    public void mostrar() {
        int op;
        do {
            System.out.println("\n== Gestión de Gatos ==");
            System.out.println("1. Registrar gato (por voluntario)");
            System.out.println("2. Actualizar estado de salud");
            System.out.println("3. Asignar / regenerar código QR");
            System.out.println("4. Listar todos");
            System.out.println("0. Volver");
            System.out.print("> ");
            op = leerEntero();

            switch (op) {
                case 1 -> registrarGato();
                case 2 -> actualizarEstado();
                case 3 -> asignarQr();
                case 4 -> listarTodos();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 0);
    }

    // --------- Opciones ---------

    private void registrarGato() {
        try {
            System.out.print("ID de voluntario: ");
            int voluntarioId = leerEntero();
            System.out.print("Nombre (opcional): ");
            String nombre = sc.nextLine().trim();
            System.out.print("Raza: ");
            String raza = sc.nextLine().trim();
            System.out.print("Edad (ENTER para omitir): ");
            String sEdad = sc.nextLine().trim();
            Integer edad = sEdad.isEmpty() ? null : Integer.parseInt(sEdad);
            System.out.print("Foto (ruta/URL, opcional): ");
            String foto = sc.nextLine().trim();

            EstadoSalud estado = elegirEstadoSalud();

            System.out.print("ID de zona: ");
            long zonaId = Long.parseLong(sc.nextLine().trim());

            Gato g = service.registrarGatoPorVoluntario(
                    voluntarioId, nombre, raza, edad, foto, estado, zonaId
            );
            System.out.println("Gato creado con ID=" + g.getId() + " | QR=" + g.getQr());
        } catch (Exception e) {
            System.err.println("Error registrando gato: " + e.getMessage());
        }
    }

    private void actualizarEstado() {
        try {
            System.out.print("ID del gato: ");
            long gatoId = Long.parseLong(sc.nextLine().trim());
            EstadoSalud nuevo = elegirEstadoSalud();
            service.actualizarEstadoSalud(gatoId, nuevo);
            System.out.println("Estado actualizado.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void asignarQr() {
        try {
            System.out.print("ID del gato: ");
            long gatoId = Long.parseLong(sc.nextLine().trim());
            String qr = service.asignarCodigoQr(gatoId);
            System.out.println("Código QR asignado: " + qr);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void listarTodos() {
        List<Gato> gatos = service.listarTodos();
        if (gatos.isEmpty()) {
            System.out.println("(Sin registros)");
            return;
        }
        System.out.println("\nID | Nombre | Estado | Zona | QR");
        for (Gato g : gatos) {
            String zona = (g.getZona() != null) ? String.valueOf(g.getZona().getId()) : "-";
            System.out.printf("%d | %s | %s | %s | %s%n",
                    g.getId(),
                    g.getNombre() == null ? "-" : g.getNombre(),
                    g.getEstadoDeSalud() == null ? "-" : g.getEstadoDeSalud().name(),
                    zona,
                    g.getQr() == null ? "-" : g.getQr()
            );
        }
    }

    // --------- Helpers ---------

    private int leerEntero() {
        String s = sc.nextLine().trim();
        return s.isEmpty() ? 0 : Integer.parseInt(s);
    }

    private EstadoSalud elegirEstadoSalud() {
        System.out.println("Estado de salud:");
        EstadoSalud[] vals = EstadoSalud.values();
        for (int i = 0; i < vals.length; i++) {
            System.out.println((i + 1) + ". " + vals[i].name());
        }
        System.out.print("> ");
        int idx = leerEntero();
        if (idx < 1 || idx > vals.length) {
            System.out.println("Opción inválida, se usará SANO.");
            return EstadoSalud.SANO;
        }
        return vals[idx - 1];
    }
}
