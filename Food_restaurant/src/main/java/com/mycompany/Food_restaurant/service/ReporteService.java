package com.mycompany.Food_restaurant.service;

import com.mycompany.Food_restaurant.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    private LocalDate parseFecha(String fecha) {
        if (fecha == null || fecha.isBlank()) return null;
        try {
            return LocalDate.parse(fecha, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            return null;
        }
    }

    // ── Resumen para las 4 tarjetas ───────────────────────────────────────────
    public Map<String, Object> obtenerResumen(String desde, String hasta) {
        LocalDate d = parseFecha(desde);
        LocalDate h = parseFecha(hasta);

        Double ventas = reporteRepository.sumVentasTotales(d, h);
        long comprobantes = reporteRepository.countComprobantes(d, h);
        long pedidos = reporteRepository.countPedidosCobrados(d, h);
        List<String> platos = reporteRepository.findPlatoMasVendido(d, h);
        String platoTop = platos.isEmpty() ? "-" : platos.get(0);

        Map<String, Object> resp = new HashMap<>();
        resp.put("ventasTotales", ventas != null ? ventas : 0.0);
        resp.put("comprobantes", comprobantes);
        resp.put("pedidos", pedidos);
        resp.put("platoMasVendido", platoTop);
        return resp;
    }

    // ── Ventas por pedido ─────────────────────────────────────────────────────
    public List<Map<String, Object>> obtenerVentasPorPedido(String desde, String hasta) {
        LocalDate d = parseFecha(desde);
        LocalDate h = parseFecha(hasta);

        List<Object[]> rows = reporteRepository.findVentasPorPedido(d, h);
        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Object[] row : rows) {
            Map<String, Object> fila = new HashMap<>();
            fila.put("nroPedido",  row[0]);
            fila.put("codCliente", row[1]);
            fila.put("tipo",       row[2]);
            fila.put("monto",      row[3]);
            fila.put("fecha",      row[4]);
            resultado.add(fila);
        }
        return resultado;
    }

    // ── Platos vendidos ───────────────────────────────────────────────────────
    public List<Map<String, Object>> obtenerPlatosVendidos(String desde, String hasta) {
        LocalDate d = parseFecha(desde);
        LocalDate h = parseFecha(hasta);

        List<Object[]> rows = reporteRepository.findPlatosVendidos(d, h);
        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Object[] row : rows) {
            Map<String, Object> fila = new HashMap<>();
            fila.put("nombre",    row[0]);
            fila.put("categoria", row[1]);
            fila.put("cantidad",  row[2]);
            fila.put("total",     row[3]);
            resultado.add(fila);
        }
        return resultado;
    }

    // ── Comprobantes emitidos ─────────────────────────────────────────────────
    public List<Map<String, Object>> obtenerComprobantes(String desde, String hasta) {
        LocalDate d = parseFecha(desde);
        LocalDate h = parseFecha(hasta);

        List<Object[]> rows = reporteRepository.findComprobantesEmitidos(d, h);
        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Object[] row : rows) {
            Map<String, Object> fila = new HashMap<>();
            fila.put("nroComprobante",   row[0]);
            fila.put("tipoComprobante",  row[1]);
            fila.put("nroPedido",        row[2]);
            fila.put("codCliente",       row[3]);
            fila.put("metodoPago",       row[4]);
            fila.put("monto",            row[5]);
            fila.put("fecha",            row[6]);
            resultado.add(fila);
        }
        return resultado;
    }
}