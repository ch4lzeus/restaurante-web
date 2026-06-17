package com.mycompany.Food_restaurant.controller;

import com.mycompany.Food_restaurant.service.PedidoService;
import com.mycompany.Food_restaurant.service.ReservaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // ── Registrar pedido desde carrito cliente ────────────────────────────────
    @PostMapping("/registrar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> registrar(
            @RequestBody Map<String, Object> body,
            HttpSession session) {

        String usuarioSesion = (String) session.getAttribute("usuarioCodigo");
        if (usuarioSesion == null) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
        }

        try {
            String codCliente = (String) body.get("codCliente");
            int esLocal = Integer.parseInt(body.get("esLocal").toString());
            String nroMesa = (String) body.getOrDefault("nroMesa", null);
            String direccion = (String) body.getOrDefault("direccion", null);
            String telefono = (String) body.getOrDefault("telefono", null);
            String descripcion = (String) body.getOrDefault("descripcion", null);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");

            if (codCliente == null || codCliente.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Cliente no identificado"));
            }

            if (items == null || items.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El pedido no tiene platos"));
            }

            if (esLocal == 0) {
                if (direccion == null || direccion.isBlank() || telefono == null || telefono.isBlank()) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Faltan datos de delivery"));
                }
            }

            String nroPedido = pedidoService.registrarPedido(
                    codCliente, esLocal, nroMesa, direccion, telefono, descripcion, items, usuarioSesion
            );

            return ResponseEntity.ok(Map.of("success", true, "nroPedido", nroPedido));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Error al registrar pedido"));
        }
    }

    // ── Cambiar estado del pedido ─────────────────────────────────────────────
    @PostMapping("/estado")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cambiarEstado(
            @RequestBody Map<String, Object> body,
            HttpSession session) {

        String usuarioSesion = (String) session.getAttribute("usuarioCodigo");
        if (usuarioSesion == null) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
        }

        String nroPedido = (String) body.get("nroPedido");
        String nuevoEstado = (String) body.get("estado");
        pedidoService.cambiarEstado(nroPedido, nuevoEstado, usuarioSesion);

        return ResponseEntity.ok(Map.of("success", true));
    }

    @GetMapping("/detalle/{nroPedido}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> detalle(@PathVariable String nroPedido, HttpSession session) {
        String usuarioSesion = (String) session.getAttribute("usuarioCodigo");
        if (usuarioSesion == null) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
        }

        Map<String, Object> pedido = pedidoService.obtenerPedidoResumen(nroPedido);
        if (pedido == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Pedido no encontrado"));
        }

        return ResponseEntity.ok(pedido);
    }
}
