package com.mycompany.Food_restaurant.service;

import com.mycompany.Food_restaurant.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
/*
*Contiene la lógica de negocio.
*Es el puente entre el Controller y el Repository.
*El Controller le pide acciones al Service, y el Service decide qué hacer.
*/

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Maneja la lógica de login
     * Retorna un Map con "estado" y, si es OK, también CodUsuario, Nombre, CodRol
     */
    public Map<String, Object> login(String username, String password) {
        try {
            Map<String, Object> result = usuarioRepository.spLoginUsuario(username, password);
            System.out.println("Login service resultado: " + result); // <-- debug
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("estado", "ERROR");
        }
    }

    public boolean actualizarPasswordPorDni(String dni, String nuevaPass) {
        try {
            int filas = usuarioRepository.actualizarPasswordPorDni(dni, nuevaPass);
            return filas > 0; // Retorna true si actualizó al menos una fila
        } catch (Exception e) {
            return false;
        }
    }

    public boolean registrarUsuario(String codUsuario, String dni, String nombre, String contrasena) {
        try {
            // Llama a UsuarioRepository
            int filas = usuarioRepository.registrarUsuario(codUsuario, dni, nombre, contrasena);
            return filas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> listarUsuarios() {
        return usuarioRepository.listarUsuarios();
    }

    public boolean registrarUsuarioAdministrativo(String codUsuario, String dni, String nombre, String codRol, String contrasena) {
        try {
            int filas = usuarioRepository.registrarUsuarioAdministrativo(codUsuario, dni, nombre, codRol, contrasena);
            return filas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editarUsuario(String codUsuario, String dni, String nombre, String codRol, String contrasena) {
        try {
            int filas = usuarioRepository.editarUsuario(codUsuario, dni, nombre, codRol, contrasena);
            return filas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean activarUsuario(String codUsuario) {
        return cambiarEstadoUsuario(codUsuario, "ACT");
    }

    public boolean desactivarUsuario(String codUsuario) {
        return cambiarEstadoUsuario(codUsuario, "INA");
    }

    private boolean cambiarEstadoUsuario(String codUsuario, String estado) {
        try {
            int filas = usuarioRepository.actualizarEstadoUsuario(codUsuario, estado);
            return filas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
