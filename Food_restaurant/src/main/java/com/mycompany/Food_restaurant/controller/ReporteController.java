package com.mycompany.Food_restaurant.controller;

import com.mycompany.Food_restaurant.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    // GET /reportes/resumen?desde=2026-01-01&hasta=2026-12-31
    @GetMapping("/resumen")
    public ResponseEntity<?> resumen(
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta) {
        try {
            return ResponseEntity.ok(reporteService.obtenerResumen(desde, hasta));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /reportes/ventas?desde=&hasta=
    @GetMapping("/ventas")
    public ResponseEntity<?> ventas(
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta) {
        try {
            return ResponseEntity.ok(reporteService.obtenerVentasPorPedido(desde, hasta));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /reportes/platos?desde=&hasta=
    @GetMapping("/platos")
    public ResponseEntity<?> platos(
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta) {
        try {
            return ResponseEntity.ok(reporteService.obtenerPlatosVendidos(desde, hasta));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /reportes/comprobantes?desde=&hasta=
    @GetMapping("/comprobantes")
    public ResponseEntity<?> comprobantes(
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta) {
        try {
            return ResponseEntity.ok(reporteService.obtenerComprobantes(desde, hasta));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}