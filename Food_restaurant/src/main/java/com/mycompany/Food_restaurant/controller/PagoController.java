package com.mycompany.Food_restaurant.controller;

import com.mycompany.Food_restaurant.service.PagoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    // GET /pagos/pedidos-cobrables → tabla izquierda
    @GetMapping("/pedidos-cobrables")
    public ResponseEntity<?> pedidosCobrables() {
        try {
            return ResponseEntity.ok(pagoService.listarPedidosCobrables());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /pagos/tipos-comprobante → select #cp-tipo
    @GetMapping("/tipos-comprobante")
    public ResponseEntity<?> tiposComprobante() {
        try {
            return ResponseEntity.ok(pagoService.listarTiposComprobante());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /pagos/metodos-pago → select #cp-mp
    @GetMapping("/metodos-pago")
    public ResponseEntity<?> metodosPago() {
        try {
            return ResponseEntity.ok(pagoService.listarMetodosPago());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /pagos/nro-comprobante?tipo=TC001 → preview del número antes de cobrar
    @GetMapping("/nro-comprobante")
    public ResponseEntity<?> nroComprobante(@RequestParam String tipo) {
        try {
            String nro = pagoService.generarNroComprobante(tipo);
            return ResponseEntity.ok(Map.of("nroComprobante", nro));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /pagos/cobrar → botón genComp()
    @PostMapping("/cobrar")
    public ResponseEntity<?> cobrar(@RequestBody Map<String, Object> body, HttpSession session) {
        try {
            String usuario = (String) session.getAttribute("usuarioCod");
            if (usuario == null) usuario = "dbo";

            String nroPedido        = (String) body.get("nroPedido");
            String codTipoComp      = (String) body.get("codTipoComprobante");
            String codMetodoPago    = (String) body.get("codMetodoPago");
            double montoRecibido    = Double.parseDouble(body.get("montoRecibido").toString());

            if (nroPedido == null || nroPedido.isBlank())
                return ResponseEntity.badRequest().body(Map.of("error", "Selecciona un pedido"));
            if (montoRecibido <= 0)
                return ResponseEntity.badRequest().body(Map.of("error", "Monto recibido inválido"));

            Map<String, Object> resultado = pagoService.cobrar(
                    nroPedido, codTipoComp, codMetodoPago, montoRecibido, usuario);

            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /pagos/emitidos → tabla inferior
    @GetMapping("/emitidos")
    public ResponseEntity<?> emitidos() {
        try {
            return ResponseEntity.ok(pagoService.listarPagosEmitidos());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}