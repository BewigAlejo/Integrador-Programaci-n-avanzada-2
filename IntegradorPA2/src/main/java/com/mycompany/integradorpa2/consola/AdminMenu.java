package com.mycompany.integradorpa2.consola;

import com.mycompany.integradorpa2.service.ZonaService;
import com.mycompany.integradorpa2.logica.*;
import com.mycompany.integradorpa2.logica.enums.Disponibilidad;
import com.mycompany.integradorpa2.logica.enums.Experiencia;
import com.mycompany.integradorpa2.service.UsuarioService;

import java.util.List;
import java.util.Scanner;

public class AdminMenu{
    private final UsuarioService service = new UsuarioService();
    private final ZonaService zonaService = new ZonaService();
    
    public void mostrar(Scanner sc) {
        int op;
        do {
            System.out.println("\n=== ADMIN ===");
            System.out.println("1) Voluntarios");
            System.out.println("2) Veterinarios");
            System.out.println("3) Familias");
            System.out.println("4) Zonas");
            System.out.println("0) Volver");
            System.out.print("> ");
            op = leerEntero(sc);

            switch (op) {
                case 1 -> menuVoluntario(sc);
                case 2 -> menuVeterinario(sc);
                case 3 -> menuFamilia(sc);
                case 4 -> menuZonas(sc);
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 0);
    }

    // -------- Voluntarios --------
    private void menuVoluntario(Scanner sc) {
        int op;
        do {
            System.out.println("\n--- Voluntarios ---");
            System.out.println("1) Crear");
            System.out.println("2) Listar");
            System.out.println("3) Editar");
            System.out.println("4) Eliminar");
            System.out.println("0) Volver");
            System.out.print("> ");
            op = leerEntero(sc);

            try {
                switch (op) {
                    case 1 -> crearVoluntario(sc);
                    case 2 -> listarVoluntarios();
                    case 3 -> editarVoluntario(sc);
                    case 4 -> eliminarVoluntario(sc);
                    case 0 -> {}
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (op != 0);
    }
    private void crearVoluntario(Scanner sc) {
        System.out.print("Nombre: "); String nombre = sc.nextLine();
        System.out.print("Email: "); String email = sc.nextLine();
        System.out.print("Teléfono: "); String telefono = sc.nextLine();
        Disponibilidad disp = null;
        while (disp == null) {
            System.out.print("Disponibilidad (ALTA, MEDIA, BAJA, NO_DISPONIBLE): ");
            try {
                disp = Disponibilidad.valueOf(sc.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Valor invalido. Intenta nuevamente.");
            }
        }
        Experiencia exp = null;
        while (exp == null) {
            System.out.print("Experiencia (NOVATO, INTERMEDIO, AVANZADO, EXPERTO): ");
            try {
                exp = Experiencia.valueOf(sc.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Valor invalido. Intenta nuevamente.");
            }
        }
        Voluntario v = service.crearVoluntario(nombre, email, telefono, disp, exp);
        System.out.println("Creado con ID: " + v.getId());
    }
    private void listarVoluntarios() {
        List<Voluntario> lista = service.listarVoluntarios();
        if (lista.isEmpty()) System.out.println("(sin registros)");
        else lista.forEach(v -> System.out.println(v.getId()+" - "+v.getNombre()+" | "+v.getEmail()+" | rol="+v.getRol()));
    }
    private void editarVoluntario(Scanner sc) {
        System.out.print("ID a editar: "); Integer id = Integer.parseInt(sc.nextLine());
        System.out.print("Nuevo nombre (ENTER omite): "); String nombre = leerOmitible(sc);
        System.out.print("Nuevo email (ENTER omite): "); String email = leerOmitible(sc);
        System.out.print("Nuevo teléfono (ENTER omite): "); String tel = leerOmitible(sc);
        System.out.print("Nueva disponibilidad (ENTER omite | ALTA, MEDIA, BAJA, NO_DISPONIBLE): ");
        String dispInput = leerOmitible(sc);
        Disponibilidad disp = null;
        if (dispInput != null && !dispInput.isBlank()) {
            try {
                disp = Disponibilidad.valueOf(dispInput.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Valor invalido para disponibilidad. No se aplicara cambio.");
            }
        }
        System.out.print("Nueva experiencia (ENTER omite | NOVATO, INTERMEDIO, AVANZADO, EXPERTO): ");
        String expInput = leerOmitible(sc);
        Experiencia exp = null;
        if (expInput != null && !expInput.isBlank()) {
            try {
                exp = Experiencia.valueOf(expInput.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Valor invalido para experiencia. No se aplicara cambio.");
            }
        }
        System.out.print("Nuevo rol (ENTER omite): "); String rol = leerOmitible(sc);
        service.actualizarVoluntario(
                                        id,
                                        n(nombre),   
                                        n(email),    
                                        n(tel),      
                                        disp,        
                                        exp,         
                                        n(rol)       
                                    );
        System.out.println("Actualizado.");
    }
    private void eliminarVoluntario(Scanner sc) {
        System.out.print("ID a eliminar: "); Integer id = Integer.parseInt(sc.nextLine());
        service.eliminarVoluntario(id);
        System.out.println("Eliminado.");
    }

    // -------- Veterinarios --------
    private void menuVeterinario(Scanner sc) {
        int op;
        do {
            System.out.println("\n--- Veterinarios ---");
            System.out.println("1) Crear");
            System.out.println("2) Listar");
            System.out.println("3) Editar");
            System.out.println("4) Eliminar");
            System.out.println("0) Volver");
            System.out.print("> ");
            op = leerEntero(sc);

            try {
                switch (op) {
                    case 1 -> crearVeterinario(sc);
                    case 2 -> listarVeterinarios();
                    case 3 -> editarVeterinario(sc);
                    case 4 -> eliminarVeterinario(sc);
                    case 0 -> {}
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (op != 0);
    }
    private void crearVeterinario(Scanner sc) {
        System.out.print("Nombre: "); String nombre = sc.nextLine();
        System.out.print("Email: "); String email = sc.nextLine();
        System.out.print("Teléfono: "); String telefono = sc.nextLine();
        System.out.print("Matrícula: "); String matricula = sc.nextLine();
        System.out.print("Especialidad: "); String esp = sc.nextLine();
        Veterinario vt = service.crearVeterinario(nombre, email, telefono, matricula, esp);
        System.out.println("Creado con ID: " + vt.getId());
    }
    private void listarVeterinarios() {
        List<Veterinario> lista = service.listarVeterinarios();
        if (lista.isEmpty()) System.out.println("(sin registros)");
        else lista.forEach(v -> System.out.println(v.getId()+" - "+v.getNombre()+" | "+v.getEmail()+" | rol="+v.getRol()));
    }
    private void editarVeterinario(Scanner sc) {
        System.out.print("ID a editar: "); Integer id = Integer.parseInt(sc.nextLine());
        System.out.print("Nuevo nombre (ENTER omite): "); String nombre = leerOmitible(sc);
        System.out.print("Nuevo email (ENTER omite): "); String email = leerOmitible(sc);
        System.out.print("Nuevo teléfono (ENTER omite): "); String tel = leerOmitible(sc);
        System.out.print("Nueva matrícula (ENTER omite): "); String mat = leerOmitible(sc);
        System.out.print("Nueva especialidad (ENTER omite): "); String esp = leerOmitible(sc);
        System.out.print("Nuevo rol (ENTER omite): "); String rol = leerOmitible(sc);
        service.actualizarVeterinario(id, n(nombre), n(email), n(tel), n(mat), n(esp), n(rol));
        System.out.println("Actualizado.");
    }
    private void eliminarVeterinario(Scanner sc) {
        System.out.print("ID a eliminar: "); Integer id = Integer.parseInt(sc.nextLine());
        service.eliminarVeterinario(id);
        System.out.println("Eliminado.");
    }

    // -------- Familias --------
    private void menuFamilia(Scanner sc) {
        int op;
        do {
            System.out.println("\n--- Familias ---");
            System.out.println("1) Crear");
            System.out.println("2) Listar");
            System.out.println("3) Editar");
            System.out.println("4) Eliminar");
            System.out.println("0) Volver");
            System.out.print("> ");
            op = leerEntero(sc);

            try {
                switch (op) {
                    case 1 -> crearFamilia(sc);
                    case 2 -> listarFamilias();
                    case 3 -> editarFamilia(sc);
                    case 4 -> eliminarFamilia(sc);
                    case 0 -> {}
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (op != 0);
    }
    private void crearFamilia(Scanner sc) {
        System.out.print("Nombre: "); String nombre = sc.nextLine();
        System.out.print("Email: "); String email = sc.nextLine();
        System.out.print("Teléfono: "); String telefono = sc.nextLine();
        System.out.print("Dirección: "); String dir = sc.nextLine();
        System.out.print("Coordenadas: "); String coord = sc.nextLine();
        System.out.print("Reputación (0-10): "); Integer rep = Integer.parseInt(sc.nextLine());
        Familia f = service.crearFamilia(nombre, email, telefono, dir, coord, rep);
        System.out.println("Creada con ID: " + f.getId());
    }
    private void listarFamilias() {
        List<Familia> lista = service.listarFamilias();
        if (lista.isEmpty()) System.out.println("(sin registros)");
        else lista.forEach(f -> System.out.println(f.getId()+" - "+f.getNombre()+" | "+f.getEmail()+" | rol="+f.getRol()));
    }
    private void editarFamilia(Scanner sc) {
        System.out.print("ID a editar: "); Integer id = Integer.parseInt(sc.nextLine());
        System.out.print("Nuevo nombre (ENTER omite): "); String nombre = leerOmitible(sc);
        System.out.print("Nuevo email (ENTER omite): "); String email = leerOmitible(sc);
        System.out.print("Nuevo teléfono (ENTER omite): "); String tel = leerOmitible(sc);
        System.out.print("Nueva dirección (ENTER omite): "); String dir = leerOmitible(sc);
        System.out.print("Nuevas coordenadas (ENTER omite): "); String coord = leerOmitible(sc);
        System.out.print("Nueva reputación (ENTER omite): "); String repStr = leerOmitible(sc);
        System.out.print("Nuevo rol (ENTER omite): "); String rol = leerOmitible(sc);
        Integer rep = repStr.isBlank() ? null : Integer.parseInt(repStr);
        service.actualizarFamilia(id, n(nombre), n(email), n(tel), n(dir), n(coord), rep, n(rol));
        System.out.println("Actualizada.");
    }
    private void eliminarFamilia(Scanner sc) {
        System.out.print("ID a eliminar: "); Integer id = Integer.parseInt(sc.nextLine());
        service.eliminarFamilia(id);
        System.out.println("Eliminada.");
    }
    
    // ================== ZONAS ==================
    private void menuZonas(Scanner sc) {
        int op;
        do {
            System.out.println("\n--- Gestión de Zonas ---");
            System.out.println("1. Crear zona");
            System.out.println("2. Listar zonas");
            System.out.println("3. Editar zona");
            System.out.println("4. Eliminar zona");
            System.out.println("0. Volver");
            System.out.print("> ");
            op = leerEntero(sc);
            switch (op) {
                case 1 -> crearZona(sc);
                case 2 -> listarZonas();
                case 3 -> editarZona(sc);
                case 4 -> eliminarZona(sc);
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 0);
    }

    private void crearZona(Scanner sc) {
        System.out.print("Nombre de la zona: ");
        String nombre = sc.nextLine();
        Double lat = leerDoubleOmitible(sc, "Latitud (ENTER para omitir): ");
        Double lon = leerDoubleOmitible(sc, "Longitud (ENTER para omitir): ");
        Zona z = zonaService.crearZona(nombre, lat, lon);
        System.out.println("Zona creada con ID: " + z.getId());
    }

    private void listarZonas() {
        List<Zona> zonas = zonaService.listarZonas();
        if (zonas.isEmpty()) {
            System.out.println("(sin registros)");
            return;
        }
        zonas.forEach(z ->
            System.out.printf("ID=%d | nombre=%s | lat=%.6f | lon=%.6f%n",
                    z.getId(),
                    z.getNombreZona(),
                    z.getCoor_lat() == null ? 0.0 : z.getCoor_lat(),
                    z.getCoor_lon() == null ? 0.0 : z.getCoor_lon())
        );
    }

    private void editarZona(Scanner sc) {
        System.out.print("ID a editar: ");
        Long id = Long.parseLong(sc.nextLine());
        System.out.print("Nuevo nombre (ENTER omite): ");
        String nombre = leerOmitible(sc);
        Double lat = leerDoubleOmitible(sc, "Nueva latitud (ENTER omite): ");
        Double lon = leerDoubleOmitible(sc, "Nueva longitud (ENTER omite): ");
        zonaService.actualizarZona(id, n(nombre), lat, lon);
        System.out.println("Actualizada.");
    }

    private void eliminarZona(Scanner sc) {
        System.out.print("ID a eliminar: ");
        Long id = Long.parseLong(sc.nextLine());
        zonaService.eliminarZona(id);
        System.out.println("Eliminada.");
    }

    // ---------- Helpers de lectura ----------
    private int leerEntero(Scanner sc) {
        try { return Integer.parseInt(sc.nextLine()); } catch (Exception e) { return -1; }
    }
    private String leerOmitible(Scanner sc) {
        String s = sc.nextLine();
        return s != null && !s.isBlank() ? s : null;
    }
    private Double leerDoubleOmitible(Scanner sc, String prompt) {
        System.out.print(prompt);
        String s = sc.nextLine();
        if (s == null || s.isBlank()) return null;
        try { return Double.parseDouble(s.replace(",", ".")); }
        catch (Exception e) { System.out.println("Valor inválido, se omite."); return null; }
    }
    private String n(String s) { return (s == null || s.isBlank()) ? null : s; }
}
