package com.mycompany.Food_restaurant.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CatalogoRepository {

    private final JdbcTemplate jdbcTemplate;

    public CatalogoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> listarCategorias() {
        String sql = """
                SELECT
                    CodCategoria AS codCategoria,
                    Nombre AS nombre,
                    UsuCreacion AS usuCreacion,
                    FecCreacion AS fecCreacion
                FROM categoria
                ORDER BY Nombre
                """;
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> listarAlimentos() {
        String sql = """
                SELECT
                    a.CodAlimento AS codAlimento,
                    a.CodAlimento AS id,
                    a.CodCategoria AS codCategoria,
                    c.Nombre AS categoria,
                    a.Nombre AS nombre,
                    a.Descripcion AS descripcion,
                    a.Rutaimagen AS img,
                    a.DescuentoPromocion AS descuentoPromocion,
                    a.Precio AS precio,
                    a.Estado AS estado,
                    a.UsuCreacion AS usuCreacion,
                    a.FecCreacion AS fecCreacion
                FROM alimento a
                INNER JOIN categoria c ON c.CodCategoria = a.CodCategoria
                ORDER BY a.FecCreacion DESC
                """;
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> listarAlimentosActivos() {
        String sql = """
                SELECT
                    a.CodAlimento AS id,
                    a.Nombre AS nombre,
                    a.Precio AS precio,
                    c.Nombre AS categoria,
                    a.Descripcion AS descripcion,
                    a.Rutaimagen AS img
                FROM alimento a
                INNER JOIN categoria c ON c.CodCategoria = a.CodCategoria
                WHERE a.Estado = 'ACT'
                ORDER BY a.Nombre
                """;
        return jdbcTemplate.queryForList(sql);
    }

    public Map<String, Object> obtenerAlimentoActivo(String codAlimento) {
        String sql = """
                SELECT
                    a.CodAlimento AS id,
                    a.Nombre AS nombre,
                    a.Precio AS precio,
                    c.Nombre AS categoria,
                    a.Descripcion AS descripcion,
                    a.Rutaimagen AS img
                FROM alimento a
                INNER JOIN categoria c ON c.CodCategoria = a.CodCategoria
                WHERE a.CodAlimento = ? AND a.Estado = 'ACT'
                """;
        return jdbcTemplate.queryForMap(sql, codAlimento);
    }

    public int registrarCategoria(String codCategoria, String nombre, String usuario) {
        String sql = "INSERT INTO categoria(CodCategoria, Nombre, UsuCreacion) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, codCategoria, nombre, usuario);
    }

    public int registrarAlimento(String codAlimento, String codCategoria, String nombre, String descripcion,
                                 String rutaImagen, double descuentoPromocion, double precio, String usuario) {
        String sql = """
                INSERT INTO alimento
                (CodAlimento, CodCategoria, Nombre, Descripcion, Rutaimagen, DescuentoPromocion, Precio, Estado, UsuCreacion)
                VALUES (?, ?, ?, ?, ?, ?, ?, 'ACT', ?)
                """;
        return jdbcTemplate.update(sql, codAlimento, codCategoria, nombre, descripcion, rutaImagen,
                descuentoPromocion, precio, usuario);
    }

    public int editarAlimento(String codAlimento, String codCategoria, String nombre, String descripcion,
                              String rutaImagen, double descuentoPromocion, double precio, String estado,
                              String usuario) {
        String sql = """
                UPDATE alimento
                SET CodCategoria = ?,
                    Nombre = ?,
                    Descripcion = ?,
                    Rutaimagen = ?,
                    DescuentoPromocion = ?,
                    Precio = ?,
                    Estado = ?,
                    UsuModificacion = ?,
                    FecModificacion = current_timestamp()
                WHERE CodAlimento = ?
                """;
        return jdbcTemplate.update(sql, codCategoria, nombre, descripcion, rutaImagen, descuentoPromocion,
                precio, estado, usuario, codAlimento);
    }

    public int actualizarEstadoAlimento(String codAlimento, String estado, String usuario) {
        String sql = """
                UPDATE alimento
                SET Estado = ?, UsuModificacion = ?, FecModificacion = current_timestamp()
                WHERE CodAlimento = ?
                """;
        return jdbcTemplate.update(sql, estado, usuario, codAlimento);
    }
}
