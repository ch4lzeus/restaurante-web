package com.mycompany.Food_restaurant.service;

import com.mycompany.Food_restaurant.repository.CatalogoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CatalogoService {

    private final CatalogoRepository catalogoRepository;

    public CatalogoService(CatalogoRepository catalogoRepository) {
        this.catalogoRepository = catalogoRepository;
    }

    public List<Map<String, Object>> listarCategorias() {
        return catalogoRepository.listarCategorias();
    }

    public List<Map<String, Object>> listarAlimentos() {
        return catalogoRepository.listarAlimentos();
    }

    public List<Map<String, Object>> listarAlimentosActivos() {
        return catalogoRepository.listarAlimentosActivos();
    }

    public Map<String, Object> obtenerAlimentoActivo(String codAlimento) {
        try {
            return catalogoRepository.obtenerAlimentoActivo(codAlimento);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean registrarCategoria(String codCategoria, String nombre, String usuario) {
        try {
            return catalogoRepository.registrarCategoria(codCategoria, nombre, usuario) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registrarAlimento(String codAlimento, String codCategoria, String nombre, String descripcion,
                                     String rutaImagen, double descuentoPromocion, double precio, String usuario) {
        try {
            return catalogoRepository.registrarAlimento(codAlimento, codCategoria, nombre, descripcion,
                    rutaImagen, descuentoPromocion, precio, usuario) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editarAlimento(String codAlimento, String codCategoria, String nombre, String descripcion,
                                  String rutaImagen, double descuentoPromocion, double precio, String estado,
                                  String usuario) {
        try {
            return catalogoRepository.editarAlimento(codAlimento, codCategoria, nombre, descripcion, rutaImagen,
                    descuentoPromocion, precio, estado, usuario) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean activarAlimento(String codAlimento, String usuario) {
        return cambiarEstadoAlimento(codAlimento, "ACT", usuario);
    }

    public boolean desactivarAlimento(String codAlimento, String usuario) {
        return cambiarEstadoAlimento(codAlimento, "INA", usuario);
    }

    private boolean cambiarEstadoAlimento(String codAlimento, String estado, String usuario) {
        try {
            return catalogoRepository.actualizarEstadoAlimento(codAlimento, estado, usuario) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
