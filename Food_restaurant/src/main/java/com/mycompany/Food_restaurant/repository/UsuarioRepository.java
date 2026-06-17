package com.mycompany.Food_restaurant.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/*
* Accede a la base de datos.
*Parecido a un DAO, pero más simplificado con Spring.
 */
@Repository
public class UsuarioRepository {

    private final JdbcTemplate jdbcTemplate;

    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Llama al procedimiento almacenado sp_login_usuario
     * Devuelve un Map con las claves:
     * - estado: "OK", "ERROR_USER", "ERROR_PASS", "INACTIVO", "ERROR"
     * - CodUsuario, Nombre, CodRol solo si estado = "OK"
     */
    public Map<String, Object> spLoginUsuario(String username, String password) {
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(
                    "CALL sp_login_usuario(?, ?)",
                    username,
                    password
            );
            System.out.println("Resultado login: " + result); // <-- útil para debug
            return result;
        } catch (Exception e) {
            e.printStackTrace(); // <-- imprime cualquier error de DB
            return Map.of("estado", "ERROR");
        }
    }

    public int actualizarPasswordPorDni(String dni, String nuevaPass) {
        String sql = "UPDATE usuario SET contrasena = ? WHERE dni = ?";
        return jdbcTemplate.update(sql, nuevaPass, dni);
    }

    // Registro de usuario cliente
    public int registrarUsuario(String codUsuario, String dni, String nombre, String contrasena) {
        // 1. Insertar en usuario
        String sqlUsr = "INSERT INTO usuario(CodUsuario, CodRol, dni, nombre, estado, contrasena, UsuCreacion) " +
                "VALUES (?, 'CLI', ?, ?, 'ACT', ?, ?)";
        int filas = jdbcTemplate.update(sqlUsr, codUsuario, dni, nombre, contrasena, codUsuario);

        // 2. Generar CodCliente automatico
        String sqlUltimo = "SELECT IFNULL(MAX(CodCliente), 'CLI000') FROM cliente";
        String ultimo = jdbcTemplate.queryForObject(sqlUltimo, String.class);
        int numero = Integer.parseInt(ultimo.substring(3));
        String codCliente = String.format("CLI%03d", numero + 1);

        // 3. Insertar en cliente
        String sqlCli = "INSERT INTO cliente(CodCliente, CodUsuario, CodTipoDocumento, NroDocumento, RazonSocial, UsuCreacion, FecCreacion) " +
                "VALUES (?, ?, 'TD001', ?, ?, ?, NOW())";
        jdbcTemplate.update(sqlCli, codCliente, codUsuario, dni, nombre, codUsuario);

        return filas;
    }

    public int registrarUsuarioAdministrativo(String codUsuario, String dni, String nombre, String codRol, String contrasena) {
        String sql = "INSERT INTO usuario(CodUsuario, CodRol, dni, nombre, estado, contrasena, UsuCreacion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, codUsuario, codRol, dni, nombre, "ACT", contrasena, codUsuario);
    }

    public int editarUsuario(String codUsuario, String dni, String nombre, String codRol, String contrasena) {
        if (contrasena == null || contrasena.isBlank()) {
            String sql = "UPDATE usuario SET dni = ?, nombre = ?, CodRol = ? WHERE CodUsuario = ?";
            return jdbcTemplate.update(sql, dni, nombre, codRol, codUsuario);
        }

        String sql = "UPDATE usuario SET dni = ?, nombre = ?, CodRol = ?, contrasena = ? WHERE CodUsuario = ?";
        return jdbcTemplate.update(sql, dni, nombre, codRol, contrasena, codUsuario);
    }

    public int actualizarEstadoUsuario(String codUsuario, String estado) {
        String sql = "UPDATE usuario SET estado = ? WHERE CodUsuario = ?";
        return jdbcTemplate.update(sql, estado, codUsuario);
    }

    public List<Map<String, Object>> listarUsuarios() {
        String sql = """
                SELECT
                    CodUsuario AS codUsuario,
                    nombre AS nombre,
                    dni AS dni,
                    CodRol AS codRol,
                    estado AS estado,
                    FecCreacion AS fecCreacion
                FROM usuario
                ORDER BY FecCreacion DESC
                """;
        return jdbcTemplate.queryForList(sql);
    }
}
