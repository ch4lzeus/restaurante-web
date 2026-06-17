package com.mycompany.Food_restaurant.service;

import com.mycompany.Food_restaurant.entity.Pedido;
import com.mycompany.Food_restaurant.entity.PedidoDetalle;
import com.mycompany.Food_restaurant.repository.CatalogoRepository;
import com.mycompany.Food_restaurant.repository.PedidoDetalleRepository;
import com.mycompany.Food_restaurant.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;

    @Autowired
    private CatalogoRepository catalogoRepository;

    // ── Generar siguiente NroPedido ───────────────────────────────────────────
    public String generarSiguienteNro() {
        List<String> codigos = pedidoRepository.findAllNroPedidoOrdenados();
        if (codigos.isEmpty()) return "PED001";
        String ultimo = codigos.get(0);
        int numero = Integer.parseInt(ultimo.substring(3));
        return String.format("PED%03d", numero + 1);
    }

    // ── Generar siguiente CodPedidoDetalle ────────────────────────────────────
    public String generarSiguienteCodDetalle() {
        List<String> codigos = pedidoDetalleRepository.findAllCodOrdenados();
        if (codigos.isEmpty()) return "DET001";
        String ultimo = codigos.get(0);
        int numero = Integer.parseInt(ultimo.substring(3));
        return String.format("DET%03d", numero + 1);
    }

    // ── Registrar pedido completo (pedido + detalles) ─────────────────────────
    @Transactional
    public String registrarPedido(String codCliente, int esLocal, String nroMesa,
                                  String direccion, String telefono, String descripcion,
                                  List<Map<String, Object>> items, String usuarioSesion) {

        // 1. Generar NroPedido
        String nroPedido = generarSiguienteNro();

        // 2. Calcular total
        double total = items.stream()
                .mapToDouble(item -> {
                    double precio = Double.parseDouble(item.get("precio").toString());
                    int cantidad = Integer.parseInt(item.get("cantidad").toString());
                    return precio * cantidad;
                })
                .sum();

        // 3. Crear pedido
        Pedido pedido = new Pedido();
        pedido.setNroPedido(nroPedido);
        pedido.setCodCliente(codCliente);
        pedido.setEsLocal(esLocal);
        pedido.setNroMesa(nroMesa);
        pedido.setDireccion(direccion);
        pedido.setTelefono(telefono);
        pedido.setDescripcion(descripcion);
        pedido.setEstado("PENDIENTE");
        pedido.setTotal(total);
        pedido.setFecPedido(LocalDateTime.now());
        pedido.setUsuCreacion(usuarioSesion);
        pedido.setFecCreacion(LocalDateTime.now());
        pedidoRepository.save(pedido);

        // 4. Crear detalles
        int contador = pedidoDetalleRepository.findAllCodOrdenados().isEmpty() ? 0
                : Integer.parseInt(pedidoDetalleRepository.findAllCodOrdenados().get(0).substring(3));

        for (Map<String, Object> item : items) {
            contador++;
            String codDetalle = String.format("DET%03d", contador);
            double precio = Double.parseDouble(item.get("precio").toString());
            int cantidad = Integer.parseInt(item.get("cantidad").toString());
            double subTotal = precio * cantidad;

            PedidoDetalle detalle = new PedidoDetalle();
            detalle.setCodPedidoDetalle(codDetalle);
            detalle.setNroPedido(nroPedido);
            detalle.setCodAlimento(item.get("id").toString());
            detalle.setCantidad(cantidad);
            detalle.setPrecio(precio);
            detalle.setDescuentoPromocion(0);
            detalle.setDescuentoMonto(0);
            detalle.setSubTotal(subTotal);
            detalle.setUsuCreacion(usuarioSesion);
            detalle.setFecCreacion(LocalDateTime.now());
            pedidoDetalleRepository.save(detalle);
        }

        return nroPedido;
    }

    // ── Listar todos los pedidos ──────────────────────────────────────────────
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarOperativos() {
        return pedidoRepository.findOperativos();
    }

    public List<Map<String, Object>> listarCocina() {
        List<Map<String, Object>> alimentos = catalogoRepository.listarAlimentos();
        return pedidoRepository.findOperativos().stream().map(pedido -> {
            List<PedidoDetalle> detalles = pedidoDetalleRepository.findByNroPedidoOrderByCodPedidoDetalle(pedido.getNroPedido());
            String platos = detalles.stream().map(det -> {
                Map<String, Object> alimento = alimentos.stream()
                        .filter(a -> String.valueOf(a.get("codAlimento")).equals(det.getCodAlimento()))
                        .findFirst()
                        .orElse(null);
                String nombre = alimento != null ? String.valueOf(alimento.get("nombre")) : det.getCodAlimento();
                return nombre + " x" + det.getCantidad();
            }).reduce((a, b) -> a + ", " + b).orElse("Sin platos");

            Map<String, Object> fila = new HashMap<>();
            fila.put("nroPedido", pedido.getNroPedido());
            fila.put("esLocal", pedido.getEsLocal());
            fila.put("nroMesa", pedido.getNroMesa());
            fila.put("direccion", pedido.getDireccion());
            fila.put("descripcion", pedido.getDescripcion());
            fila.put("estado", pedido.getEstado());
            fila.put("platos", platos);
            return fila;
        }).toList();
    }

    public List<Pedido> listarDeliveryOperativos() {
        return pedidoRepository.findDeliveryOperativos();
    }

    public List<PedidoDetalle> listarDetallesPedido(String nroPedido) {
        return pedidoDetalleRepository.findByNroPedidoOrderByCodPedidoDetalle(nroPedido);
    }

    public Map<String, Object> obtenerPedidoResumen(String nroPedido) {
        Pedido pedido = pedidoRepository.findById(nroPedido).orElse(null);
        if (pedido == null) return null;

        List<Map<String, Object>> alimentos = catalogoRepository.listarAlimentos();
        List<Map<String, Object>> detalles = pedidoDetalleRepository.findByNroPedidoOrderByCodPedidoDetalle(nroPedido).stream().map(det -> {
            Map<String, Object> alimento = alimentos.stream()
                    .filter(a -> String.valueOf(a.get("codAlimento")).equals(det.getCodAlimento()))
                    .findFirst()
                    .orElse(null);

            String nombre = alimento != null ? String.valueOf(alimento.get("nombre")) : det.getCodAlimento();
            String categoria = alimento != null ? String.valueOf(alimento.get("categoria")) : "";

            Map<String, Object> fila = new HashMap<>();
            fila.put("codPedidoDetalle", det.getCodPedidoDetalle());
            fila.put("codAlimento", det.getCodAlimento());
            fila.put("nombre", nombre);
            fila.put("categoria", categoria);
            fila.put("cantidad", det.getCantidad());
            fila.put("precio", det.getPrecio());
            fila.put("subTotal", det.getSubTotal());
            return fila;
        }).toList();

        Map<String, Object> data = new HashMap<>();
        data.put("nroPedido", pedido.getNroPedido());
        data.put("codCliente", pedido.getCodCliente());
        data.put("esLocal", pedido.getEsLocal());
        data.put("nroMesa", pedido.getNroMesa());
        data.put("direccion", pedido.getDireccion());
        data.put("telefono", pedido.getTelefono());
        data.put("descripcion", pedido.getDescripcion());
        data.put("estado", pedido.getEstado());
        data.put("fecPedido", pedido.getFecPedido());
        data.put("total", pedido.getTotal());
        data.put("detalles", detalles);
        return data;
    }

    // ── Conteos para tarjetas del dashboard ───────────────────────────────────
    public long contarPendientes() {
        return pedidoRepository.countByEstado("PENDIENTE");
    }

    public long contarEnCocina() {
        return pedidoRepository.countByEstado("PREPARANDO");
    }

    public long contarListos() {
        return pedidoRepository.countByEstado("LISTO");
    }

    public long contarDelivery() {
        return pedidoRepository.countDeliveryActivos();
    }

    // ── Cambiar estado del pedido ─────────────────────────────────────────────
    public void cambiarEstado(String nroPedido, String nuevoEstado, String usuarioSesion) {
        Pedido pedido = pedidoRepository.findById(nroPedido).orElse(null);
        if (pedido != null) {
            pedido.setEstado(nuevoEstado);
            pedido.setUsuModificacion(usuarioSesion);
            pedido.setFecModificacion(LocalDateTime.now());
            pedidoRepository.save(pedido);
        }
    }

    // Pedidos creados hoy
    public long contarPedidosHoy() {
        return pedidoRepository.count(); // total histórico
    }
}
