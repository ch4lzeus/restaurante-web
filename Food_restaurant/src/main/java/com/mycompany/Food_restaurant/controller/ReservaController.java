package com.mycompany.Food_restaurant.controller;

import com.mycompany.Food_restaurant.entity.Reserva;
import com.mycompany.Food_restaurant.service.ReservaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    private String dashboardReservas(String msg) {
        return "redirect:/dashboard?sec=pedidos&tab=reserva&msg=" + msg;
    }

    // ── Siguiente codigo automatico ───────────────────────────────────────────
    @GetMapping("/siguiente-cod")
    @ResponseBody
    public String siguienteCod() {
        return reservaService.generarSiguienteCod();
    }

    // ── Registrar nueva reserva ───────────────────────────────────────────────
    @PostMapping("/registrar")
    public String registrar(
            @RequestParam String codReserva,
            @RequestParam String nroMesa,
            @RequestParam String codCliente,
            @RequestParam String fecReserva,
            @RequestParam String estado,
            HttpSession session) {

        String usuarioSesion = (String) session.getAttribute("usuarioCodigo");
        if (usuarioSesion == null) return "redirect:/login";

        if (reservaService.buscarPorId(codReserva) != null) {
            return dashboardReservas("resDup");
        }

        Reserva reserva = new Reserva();
        reserva.setCodReserva(codReserva.trim().toUpperCase());
        reserva.setNroMesa(nroMesa);
        reserva.setCodCliente(codCliente);
        reserva.setFecReserva(LocalDateTime.parse(fecReserva));
        reserva.setEstado(estado);

        reservaService.registrar(reserva, usuarioSesion);
        return dashboardReservas("resOk");
    }

    // ── Cancelar reserva ──────────────────────────────────────────────────────
    @GetMapping("/cancelar/{codReserva}")
    public String cancelar(
            @PathVariable String codReserva,
            HttpSession session) {

        String usuarioSesion = (String) session.getAttribute("usuarioCodigo");
        if (usuarioSesion == null) return "redirect:/login";

        reservaService.cancelar(codReserva, usuarioSesion);
        return dashboardReservas("resCan");
    }

    // ── Reactivar reserva ─────────────────────────────────────────────────────
    @GetMapping("/reactivar/{codReserva}")
    public String reactivar(
            @PathVariable String codReserva,
            HttpSession session) {

        String usuarioSesion = (String) session.getAttribute("usuarioCodigo");
        if (usuarioSesion == null) return "redirect:/login";

        reservaService.reactivar(codReserva, usuarioSesion);
        return dashboardReservas("resAct");
    }

    @PostMapping("/tomar-pedido")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> tomarPedido(
            @RequestBody Map<String, Object> body,
            HttpSession session) {

        String usuarioSesion = (String) session.getAttribute("usuarioCodigo");
        if (usuarioSesion == null) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
        }

        try {
            String codReserva = String.valueOf(body.get("codReserva"));
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");

            Map<String, Object> resp = reservaService.tomarPedido(codReserva, items, usuarioSesion);
            if (resp.containsKey("error")) {
                return ResponseEntity.badRequest().body(resp);
            }
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Error al tomar pedido desde reserva"));
        }
    }
}
