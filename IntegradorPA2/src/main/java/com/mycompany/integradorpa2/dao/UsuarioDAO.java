package com.mycompany.integradorpa2.dao;

import com.mycompany.integradorpa2.logica.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioDAO {
    Optional<Usuario> buscarPorId(int id);
    Optional<Usuario> buscarPorEmail(String email);
    List<Usuario> listarTodos();

    // utilidades por tipo, si las querés expuestas
    List<? extends Usuario> listarFamilias();
    List<? extends Usuario> listarVoluntarios();
    List<? extends Usuario> listarVeterinarios();
}
