package com.mycompany.integradorpa2.service;

import com.mycompany.integradorpa2.dao.GatoDAO;
import com.mycompany.integradorpa2.dao.GatoDAOJpa;
import com.mycompany.integradorpa2.dao.VoluntarioDAO;
import com.mycompany.integradorpa2.dao.VoluntarioDAOJpa;
import com.mycompany.integradorpa2.dao.ZonaDAO;
import com.mycompany.integradorpa2.dao.ZonaDAOJpa;
import com.mycompany.integradorpa2.logica.Gato;
import com.mycompany.integradorpa2.logica.Voluntario;
import com.mycompany.integradorpa2.logica.Zona;
import com.mycompany.integradorpa2.logica.enums.EstadoSalud;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.List;

public class GatoService {

    private final GatoDAO gatoDao = new GatoDAOJpa();
    private final VoluntarioDAO voluntarioDao = new VoluntarioDAOJpa();
    private final ZonaDAO zonaDao = new ZonaDAOJpa();

    // -------------------- REGISTRO DE GATO POR VOLUNTARIO --------------------
    public Gato registrarGatoPorVoluntario(int voluntarioId, String nombre, String raza,
                                           Integer edad, String foto, EstadoSalud estadoSalud,
                                           Long zonaId) {

        // Buscar voluntario y zona
        Voluntario voluntario = voluntarioDao.buscarPorId(voluntarioId)
                .orElseThrow(() -> new IllegalArgumentException("Voluntario no encontrado con ID: " + voluntarioId));

        Zona zona = zonaDao.buscarPorId(zonaId)
                .orElseThrow(() -> new IllegalArgumentException("Zona no encontrada con ID: " + zonaId));

        // Crear el gato
        Gato gato = new Gato();
        gato.setNombre(nombre);
        gato.setRaza(raza);
        gato.setEdad(edad);
        gato.setFoto(foto);
        gato.setEstadoDeSalud(estadoSalud != null ? estadoSalud : EstadoSalud.SANO);
        gato.setZona(zona);
        gato.setAdoptado(false);

        // Generar y asignar QR único
        gato.setQr(generarCodigoQrUnico());

        // Guardar el gato en la BD
        return gatoDao.crear(gato);
    }

    // -------------------- ACTUALIZAR ESTADO DE SALUD --------------------
    public void actualizarEstadoSalud(Long gatoId, EstadoSalud nuevoEstado) {
        Gato gato = gatoDao.buscarPorId(gatoId)
                .orElseThrow(() -> new IllegalArgumentException("Gato no encontrado con ID: " + gatoId));

        gato.setEstadoDeSalud(nuevoEstado);
        gatoDao.actualizar(gato);
    }

    // -------------------- ASIGNAR / REGENERAR CÓDIGO QR --------------------
    public String asignarCodigoQr(Long gatoId) {
        Gato gato = gatoDao.buscarPorId(gatoId)
                .orElseThrow(() -> new IllegalArgumentException("Gato no encontrado con ID: " + gatoId));

        String nuevoQr = generarCodigoQrUnico();
        gato.setQr(nuevoQr);
        gatoDao.actualizar(gato);
        return nuevoQr;
    }

    // -------------------- LISTAR TODOS --------------------
    public List<Gato> listarTodos() {
        return gatoDao.listarTodos();
    }

    // -------------------- GENERAR CÓDIGO QR ÚNICO --------------------
    private String generarCodigoQrUnico() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[6];
        random.nextBytes(bytes);
        return "GATO-" + HexFormat.of().formatHex(bytes).toUpperCase();
    }
}
