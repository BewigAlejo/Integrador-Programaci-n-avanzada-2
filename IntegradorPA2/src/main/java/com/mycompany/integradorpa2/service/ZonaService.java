package com.mycompany.integradorpa2.service;

import com.mycompany.integradorpa2.dao.ZonaDAO;
import com.mycompany.integradorpa2.dao.ZonaDAOJpa;
import com.mycompany.integradorpa2.logica.Zona;

import java.util.List;
import java.util.Optional;

public class ZonaService {

    private final ZonaDAO zonaDao = new ZonaDAOJpa();

    // CREATE
    public Zona crearZona(String nombreZona, Double lat, Double lon) {
        if (nombreZona == null || nombreZona.isBlank()) {
            throw new IllegalArgumentException("El nombre de la zona es obligatorio.");
        }
        Zona z = new Zona();
        z.setNombreZona(nombreZona.trim());
        z.setCoor_lat(lat);
        z.setCoor_lon(lon);
        return zonaDao.crear(z);
    }

    // READ
    public List<Zona> listarZonas() {
        return zonaDao.listarTodos();
    }

    public Optional<Zona> zonaPorId(Long id) {
        return zonaDao.buscarPorId(id);
    }

    // UPDATE
    public Zona actualizarZona(Long id, String nombreZona, Double lat, Double lon) {
        Zona z = zonaDao.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Zona no encontrada: " + id));

        if (nombreZona != null && !nombreZona.isBlank()) z.setNombreZona(nombreZona.trim());
        if (lat != null)  z.setCoor_lat(lat);
        if (lon != null)  z.setCoor_lon(lon);

        return zonaDao.actualizar(z);
    }

    // DELETE
    public void eliminarZona(Long id) {
        zonaDao.eliminar(id);
    }
}
