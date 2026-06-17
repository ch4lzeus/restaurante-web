package com.mycompany.Food_restaurant.service;

import com.mycompany.Food_restaurant.entity.*;
import com.mycompany.Food_restaurant.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PagoService {

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private PedidoDetalleRepository pedidoDetalleRepository;
    @Autowired private ComprobanteRepository comprobanteRepository;
    @Autowired private ComprobanteDetalleRepository comprobanteDetalleRepository;
    @Autowired private CorrelativoComprobanteRepository correlativoRepository;
    @Autowired private PagoRepository pagoRepository;
    @Autowired private TipoComprobanteRepository tipoComprobanteRepository;
    @Autowired private MetodoPagoRepository metodoPagoRepository;
    @Autowired private CatalogoRepository catalogoRepository;

    // ── Pedidos en estado LISTO (cobrables) ───────────────────────────────────
    public List<Map<String, Object>> listarPedidosCobrables() {
        List<Pedido> pedidos = pedidoRepository.findByEstado("LISTO");
        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Pedido p : pedidos) {
            Map<String, Object> fila = new HashMap<>();
            fila.put("nroPedido", p.getNroPedido());
            fila.put("codCliente", p.getCodCliente());
            fila.put("esLocal", p.getEsLocal() == 1 ? "Local" : "Delivery");
            fila.put("total", p.getTotal());
            fila.put("estado", p.getEstado());
            resultado.add(fila);
        }
        return resultado;
    }

    // ── Catálogos para los selects ────────────────────────────────────────────
    public List<TipoComprobante> listarTiposComprobante() {
        return tipoComprobanteRepository.findAll();
    }

    public List<MetodoPago> listarMetodosPago() {
        return metodoPagoRepository.findAll();
    }

    // ── Generar NroComprobante desde correlativo ──────────────────────────────
    public String generarNroComprobante(String codTipoComprobante) {
        Optional<CorrelativoComprobante> opt = correlativoRepository.findByCodTipoComprobante(codTipoComprobante);
        if (opt.isEmpty()) {
            // Si no existe correlativo, retornar uno de muestra
            return "B001-00001";
        }
        CorrelativoComprobante corr = opt.get();
        int siguiente = corr.getCorrelativoActual() + 1;
        return corr.getSerie() + "-" + String.format("%05d", siguiente);
    }

    // ── Cobrar: registra comprobante + detalle + pago + actualiza correlativo ─
    @Transactional
    public Map<String, Object> cobrar(String nroPedido, String codTipoComprobante,
                                      String codMetodoPago, double montoRecibido,
                                      String usuarioSesion) {

        // 1. Validar pedido
        Pedido pedido = pedidoRepository.findById(nroPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + nroPedido));

        if (!"LISTO".equals(pedido.getEstado())) {
            throw new RuntimeException("El pedido " + nroPedido + " no está en estado LISTO");
        }

        // 2. Obtener y actualizar correlativo
        CorrelativoComprobante corr = correlativoRepository.findByCodTipoComprobante(codTipoComprobante)
                .orElseThrow(() -> new RuntimeException("No existe correlativo para el tipo: " + codTipoComprobante));

        int nuevoCorrelativo = corr.getCorrelativoActual() + 1;
        String nroComprobante = corr.getSerie() + "-" + String.format("%05d", nuevoCorrelativo);

        corr.setCorrelativoActual(nuevoCorrelativo);
        corr.setUsuModificacion(usuarioSesion);
        corr.setFecModificacion(LocalDateTime.now());
        correlativoRepository.save(corr);

        // 3. Generar CodComprobante
        List<String> cods = comprobanteRepository.findAllCodOrdenados();
        String codComprobante;
        if (cods.isEmpty()) {
            codComprobante = "COM001";
        } else {
            int num = Integer.parseInt(cods.get(0).substring(3)) + 1;
            codComprobante = String.format("COM%03d", num);
        }

        // 4. Crear comprobante
        Comprobante comprobante = new Comprobante();
        comprobante.setCodComprobante(codComprobante);
        comprobante.setCodTipoComprobante(codTipoComprobante);
        comprobante.setNroPedido(nroPedido);
        comprobante.setCodCliente(pedido.getCodCliente());
        comprobante.setNroComprobante(nroComprobante);
        comprobante.setEstado("EMITIDO");
        comprobante.setFecEmision(LocalDateTime.now());
        comprobante.setUsuCreacion(usuarioSesion);
        comprobante.setFecCreacion(LocalDateTime.now());
        comprobanteRepository.save(comprobante);

        // 5. Copiar detalles desde pedido_detalle → comprobante_detalle
        List<PedidoDetalle> detallesPedido = pedidoDetalleRepository
                .findByNroPedidoOrderByCodPedidoDetalle(nroPedido);

        List<String> codsDet = comprobanteDetalleRepository.findAllCodOrdenados();
        int contDet = codsDet.isEmpty() ? 0 : Integer.parseInt(codsDet.get(0).substring(3));

        for (PedidoDetalle pd : detallesPedido) {
            contDet++;
            ComprobanteDetalle cd = new ComprobanteDetalle();
            cd.setCodComprobanteDetalle(String.format("CDT%03d", contDet));
            cd.setCodComprobante(codComprobante);
            cd.setCodAlimento(pd.getCodAlimento());
            cd.setCantidad(pd.getCantidad());
            cd.setPrecio(pd.getPrecio());
            cd.setDescuentoPromocion(pd.getDescuentoPromocion());
            cd.setDescuentoMonto(pd.getDescuentoMonto());
            cd.setSubTotal(pd.getSubTotal());
            cd.setUsuCreacion(usuarioSesion);
            cd.setFecCreacion(LocalDateTime.now());
            comprobanteDetalleRepository.save(cd);
        }

        // 6. Registrar pago (PK = CodComprobante, sin CodPago separado)
        Pago pago = new Pago();
        pago.setCodComprobante(codComprobante);
        pago.setCodMetodoPago(codMetodoPago);
        pago.setMonto(pedido.getTotal());
        pago.setUsuCreacion(usuarioSesion);
        pago.setFecCreacion(LocalDateTime.now());
        pagoRepository.save(pago);

        // 8. Actualizar estado del pedido → COBRADO
        pedido.setEstado("COBRADO");
        pedido.setUsuModificacion(usuarioSesion);
        pedido.setFecModificacion(LocalDateTime.now());
        pedidoRepository.save(pedido);

        // 9. Respuesta
        double total = pedido.getTotal();
        double vuelto = Math.max(0, montoRecibido - total);
        Map<String, Object> resp = new HashMap<>();
        resp.put("codComprobante", codComprobante);
        resp.put("nroComprobante", nroComprobante);
        resp.put("total", total);
        resp.put("vuelto", vuelto);
        return resp;
    }

    // ── Listar pagos emitidos ─────────────────────────────────────────────────
    public List<Map<String, Object>> listarPagosEmitidos() {
        List<Comprobante> comprobantes = comprobanteRepository.findAllOrderByFecEmisionDesc();
        List<MetodoPago> metodos = metodoPagoRepository.findAll();
        List<TipoComprobante> tipos = tipoComprobanteRepository.findAll();
        List<Pago> pagos = pagoRepository.findAll();

        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Comprobante c : comprobantes) {
            // Buscar pago asociado
            Pago pago = pagos.stream()
                    .filter(p -> p.getCodComprobante().equals(c.getCodComprobante()))
                    .findFirst().orElse(null);

            String nombreMetodo = pago == null ? "-" :
                    metodos.stream()
                            .filter(m -> m.getCodMetodoPago().equals(pago.getCodMetodoPago()))
                            .map(MetodoPago::getNombre)
                            .findFirst().orElse(pago.getCodMetodoPago());

            String nombreTipo = tipos.stream()
                    .filter(t -> t.getCodTipoComprobante().equals(c.getCodTipoComprobante()))
                    .map(TipoComprobante::getNombre)
                    .findFirst().orElse(c.getCodTipoComprobante());

            Map<String, Object> fila = new HashMap<>();
            fila.put("codComprobante", c.getCodComprobante());
            fila.put("nroComprobante", c.getNroComprobante());
            fila.put("tipoComprobante", nombreTipo);
            fila.put("nroPedido", c.getNroPedido());
            fila.put("codCliente", c.getCodCliente());
            fila.put("metodoPago", nombreMetodo);
            fila.put("montoTotal", pago != null ? pago.getMonto() : 0);
            fila.put("fecEmision", c.getFecEmision());
            resultado.add(fila);
        }
        return resultado;
    }

    public double totalCobradoHoy() {
        Double total = pagoRepository.sumMontoHoy();
        return total != null ? total : 0.0;
    }
}