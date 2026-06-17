package com.mycompany.Food_restaurant.service;

import com.mycompany.Food_restaurant.entity.Mesa;
import com.mycompany.Food_restaurant.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    // Listar todas las mesas
    public List<Mesa> listarTodas() {
        return mesaRepository.findAll();
    }

    // Registrar nueva mesa
    public void registrar(Mesa mesa, String usuarioSesion) {
        mesa.setUsuCreacion(usuarioSesion);
        mesa.setFecCreacion(LocalDateTime.now());
        mesaRepository.save(mesa);
    }

    // Buscar por NroMesa
    public Mesa buscarPorId(String nroMesa) {
        return mesaRepository.findById(nroMesa).orElse(null);
    }

    // Editar mesa existente
    public void editar(String nroMesa, String descripcion, int capacidad, String estado, String usuarioSesion) {
        Mesa mesa = mesaRepository.findById(nroMesa).orElse(null);
        if (mesa != null) {
            mesa.setDescripcion(descripcion);
            mesa.setCapacidad(capacidad);
            mesa.setEstado(estado);
            mesa.setUsuModificacion(usuarioSesion);
            mesa.setFecModificacion(LocalDateTime.now());
            mesaRepository.save(mesa);
        }
    }

    // Desactivar mesa (ACT -> INA)
    public void desactivar(String nroMesa, String usuarioSesion) {
        Mesa mesa = mesaRepository.findById(nroMesa).orElse(null);
        if (mesa != null) {
            mesa.setEstado("INA");
            mesa.setUsuModificacion(usuarioSesion);
            mesa.setFecModificacion(LocalDateTime.now());
            mesaRepository.save(mesa);
        }
    }

    // Activar mesa (INA -> ACT)
    public void activar(String nroMesa, String usuarioSesion) {
        Mesa mesa = mesaRepository.findById(nroMesa).orElse(null);
        if (mesa != null) {
            mesa.setEstado("ACT");
            mesa.setUsuModificacion(usuarioSesion);
            mesa.setFecModificacion(LocalDateTime.now());
            mesaRepository.save(mesa);
        }
    }

    public String generarSiguienteNro() {
        List<String> codigos = mesaRepository.findAllNroMesaOrdenados();
        if (codigos.isEmpty()) return "M001";
        String ultimo = codigos.get(0); // ej: "M004"
        int numero = Integer.parseInt(ultimo.substring(1)); // 4
        return String.format("M%03d", numero + 1); // "M005"
    }

    public List<Mesa> listarActivas() {
        return mesaRepository.findAll().stream()
                .filter(m -> "ACT".equals(m.getEstado()))
                .collect(java.util.stream.Collectors.toList());
    }
}