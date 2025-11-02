package com.mycompany.integradorpa2.service;

import com.mycompany.integradorpa2.dao.*;
import com.mycompany.integradorpa2.logica.*;
import com.mycompany.integradorpa2.logica.enums.Disponibilidad;
import com.mycompany.integradorpa2.logica.enums.Experiencia;

import java.util.List;
import java.util.Optional;

public class UsuarioService {

    private final VoluntarioDAO voluntarioDao = new VoluntarioDAOJpa();
    private final VeterinarioDAO veterinarioDao = new VeterinarioDAOJpa();
    private final FamiliaDAO familiaDao = new FamiliaDAOJpa();

    // ---------- VOLUNTARIO ----------
    public Voluntario crearVoluntario(String nombre, String email, String telefono,
                                      Disponibilidad disponibilidad, Experiencia experiencia) {
        Voluntario v = new Voluntario();
        v.setNombre(nombre);
        v.setEmail(email);
        v.setTelefono(telefono);
        v.setRol("VOLUNTARIO");
        // Defaults por si llegan null
        v.setDisponibilidad(disponibilidad != null ? disponibilidad : Disponibilidad.MEDIA);
        v.setExperiencia(experiencia != null ? experiencia : Experiencia.NOVATO);
        return voluntarioDao.crear(v);
    }

    public List<Voluntario> listarVoluntarios() { return voluntarioDao.listarTodos(); }
    public Optional<Voluntario> voluntarioPorId(Integer id) { return voluntarioDao.buscarPorId(id); }

    public Voluntario actualizarVoluntario(Integer id, String nombre, String email, String telefono,
                                           Disponibilidad disponibilidad, Experiencia experiencia, String rol) {
        Voluntario v = voluntarioDao.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Voluntario no encontrado: " + id));

        if (nombre != null) v.setNombre(nombre);
        if (email != null) v.setEmail(email);
        if (telefono != null) v.setTelefono(telefono);
        if (disponibilidad != null) v.setDisponibilidad(disponibilidad);
        if (experiencia != null) v.setExperiencia(experiencia);
        if (rol != null) v.setRol(rol);

        return voluntarioDao.actualizar(v);
    }

    public void eliminarVoluntario(Integer id) { voluntarioDao.eliminar(id); }

    // ---------- VETERINARIO ----------
    public Veterinario crearVeterinario(String nombre, String email, String telefono,
                                        String matricula, String especialidad) {
        Veterinario vt = new Veterinario();
        vt.setNombre(nombre);
        vt.setEmail(email);
        vt.setTelefono(telefono);
        vt.setRol("VETERINARIO");
        vt.setMatricula(matricula);
        vt.setEspecialidad(especialidad);
        return veterinarioDao.crear(vt);
    }

    public List<Veterinario> listarVeterinarios() { return veterinarioDao.listarTodos(); }
    public Optional<Veterinario> veterinarioPorId(Integer id) { return veterinarioDao.buscarPorId(id); }

    public Veterinario actualizarVeterinario(Integer id, String nombre, String email, String telefono,
                                             String matricula, String especialidad, String rol) {
        Veterinario vt = veterinarioDao.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Veterinario no encontrado: " + id));
        if (nombre != null) vt.setNombre(nombre);
        if (email != null) vt.setEmail(email);
        if (telefono != null) vt.setTelefono(telefono);
        if (matricula != null) vt.setMatricula(matricula);
        if (especialidad != null) vt.setEspecialidad(especialidad);
        if (rol != null) vt.setRol(rol);
        return veterinarioDao.actualizar(vt);
    }

    public void eliminarVeterinario(Integer id) { veterinarioDao.eliminar(id); }

    // ---------- FAMILIA ----------
    public Familia crearFamilia(String nombre, String email, String telefono,
                                String direccion, String coordenadas, Integer reputacion) {
        Familia f = new Familia();
        f.setNombre(nombre);
        f.setEmail(email);
        f.setTelefono(telefono);
        f.setRol("FAMILIA");
        f.setDireccion(direccion);
        f.setCoordenadas(coordenadas);
        f.setReputacion(reputacion != null ? reputacion : 0);
        return familiaDao.crear(f);
    }

    public List<Familia> listarFamilias() { return familiaDao.listarTodos(); }
    public Optional<Familia> familiaPorId(Integer id) { return familiaDao.buscarPorId(id); }

    public Familia actualizarFamilia(Integer id, String nombre, String email, String telefono,
                                     String direccion, String coordenadas, Integer reputacion, String rol) {
        Familia f = familiaDao.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Familia no encontrada: " + id));
        if (nombre != null) f.setNombre(nombre);
        if (email != null) f.setEmail(email);
        if (telefono != null) f.setTelefono(telefono);
        if (direccion != null) f.setDireccion(direccion);
        if (coordenadas != null) f.setCoordenadas(coordenadas);
        if (reputacion != null) f.setReputacion(reputacion);
        if (rol != null) f.setRol(rol);
        return familiaDao.actualizar(f);
    }

    public void eliminarFamilia(Integer id) { familiaDao.eliminar(id); }
}
