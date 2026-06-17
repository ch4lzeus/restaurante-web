package com.mycompany.Food_restaurant.service;

import com.mycompany.Food_restaurant.entity.Cliente;
import com.mycompany.Food_restaurant.entity.Reserva;
import com.mycompany.Food_restaurant.repository.ClienteRepository;
import com.mycompany.Food_restaurant.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PedidoService pedidoService;

    // Listar todas las reservas
    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    // Listar todos los clientes (para el select del modal)
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    // Buscar reserva por id
    public Reserva buscarPorId(String codReserva) {
        return reservaRepository.findById(codReserva).orElse(null);
    }

    // Generar siguiente codigo automatico
    public String generarSiguienteCod() {
        List<String> codigos = reservaRepository.findAllCodReservaOrdenados();
        if (codigos.isEmpty()) return "RES001";
        String ultimo = codigos.get(0); // ej: "RES003"
        int numero = Integer.parseInt(ultimo.substring(3)); // 3
        return String.format("RES%03d", numero + 1); // "RES004"
    }

    // Registrar nueva reserva
    public void registrar(Reserva reserva, String usuarioSesion) {
        reserva.setUsuCreacion(usuarioSesion);
        reserva.setFecCreacion(LocalDateTime.now());
        reservaRepository.save(reserva);
    }

    // Cancelar reserva (ACT -> INA)
    public void cancelar(String codReserva, String usuarioSesion) {
        Reserva reserva = reservaRepository.findById(codReserva).orElse(null);
        if (reserva != null && "ACT".equals(reserva.getEstado())) {
            reserva.setEstado("INA");
            reserva.setUsuModificacion(usuarioSesion);
            reserva.setFecModificacion(LocalDateTime.now());
            reservaRepository.save(reserva);
        }
    }

    // Reactivar reserva (INA -> ACT)
    public void reactivar(String codReserva, String usuarioSesion) {
        Reserva reserva = reservaRepository.findById(codReserva).orElse(null);
        if (reserva != null && "INA".equals(reserva.getEstado())) {
            reserva.setEstado("ACT");
            reserva.setUsuModificacion(usuarioSesion);
            reserva.setFecModificacion(LocalDateTime.now());
            reservaRepository.save(reserva);
        }
    }

    public Map<String, Object> tomarPedido(String codReserva, List<Map<String, Object>> items, String usuarioSesion) {
        Reserva reserva = reservaRepository.findById(codReserva).orElse(null);
        if (reserva == null || !"ACT".equals(reserva.getEstado())) {
            return Map.of("error", "La reserva no esta disponible para tomar pedido");
        }
        if (items == null || items.isEmpty()) {
            return Map.of("error", "El pedido no tiene platos");
        }

        String nroPedido = pedidoService.registrarPedido(
                reserva.getCodCliente(),
                1,
                reserva.getNroMesa(),
                null,
                null,
                "Pedido desde reserva " + reserva.getCodReserva(),
                items,
                usuarioSesion
        );

        reserva.setEstado("ATE");
        reserva.setUsuModificacion(usuarioSesion);
        reserva.setFecModificacion(LocalDateTime.now());
        reservaRepository.save(reserva);

        Map<String, Object> resp = new HashMap<>();
        resp.put("nroPedido", nroPedido);
        resp.put("codReserva", codReserva);
        return resp;
    }
}
