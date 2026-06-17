package com.mycompany.Food_restaurant.controller;

import com.mycompany.Food_restaurant.entity.Mesa;
import com.mycompany.Food_restaurant.service.MesaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mesas")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    private String dashboardMesas(String msg) {
        return "redirect:/dashboard?sec=mesas&msg=" + msg;
    }

    // ── Registrar nueva mesa ──────────────────────────────────────────────────
    @PostMapping("/registrar")
    public String registrar(
            @RequestParam String nroMesa,
            @RequestParam(required = false) String descripcion,
            @RequestParam int capacidad,
            @RequestParam String estado,
            HttpSession session,
            RedirectAttributes ra) {

        String usuarioSesion = (String) session.getAttribute("usuarioCodigo");
        if (usuarioSesion == null) return "redirect:/login";

        // Validar que no exista ya
        if (mesaService.buscarPorId(nroMesa) != null) {
            ra.addAttribute("mMsg", "Ya existe una mesa con ese numero.");
            return dashboardMesas("mesaDup");
        }

        Mesa mesa = new Mesa();
        mesa.setNroMesa(nroMesa.trim().toUpperCase());
        mesa.setDescripcion(descripcion);
        mesa.setCapacidad(capacidad);
        mesa.setEstado(estado);

        mesaService.registrar(mesa, usuarioSesion);
        ra.addAttribute("mMsg", "Mesa registrada correctamente.");
        return dashboardMesas("mesaOk");
    }

    // ── Editar mesa ───────────────────────────────────────────────────────────
    @PostMapping("/editar")
    public String editar(
            @RequestParam String nroMesa,
            @RequestParam(required = false) String descripcion,
            @RequestParam int capacidad,
            @RequestParam String estado,
            HttpSession session,
            RedirectAttributes ra) {

        String usuarioSesion = (String) session.getAttribute("usuarioCodigo");
        if (usuarioSesion == null) return "redirect:/login";

        mesaService.editar(nroMesa, descripcion, capacidad, estado, usuarioSesion);
        ra.addAttribute("mMsg", "Mesa actualizada correctamente.");
        return dashboardMesas("mesaEdit");
    }

    // ── Desactivar mesa ───────────────────────────────────────────────────────
    @GetMapping("/desactivar/{nroMesa}")
    public String desactivar(
            @PathVariable String nroMesa,
            HttpSession session,
            RedirectAttributes ra) {

        String usuarioSesion = (String) session.getAttribute("usuarioCodigo");
        if (usuarioSesion == null) return "redirect:/login";

        mesaService.desactivar(nroMesa, usuarioSesion);
        ra.addAttribute("mMsg", "Mesa desactivada.");
        return dashboardMesas("mesaDes");
    }

    // ── Activar mesa ──────────────────────────────────────────────────────────
    @GetMapping("/activar/{nroMesa}")
    public String activar(
            @PathVariable String nroMesa,
            HttpSession session,
            RedirectAttributes ra) {

        String usuarioSesion = (String) session.getAttribute("usuarioCodigo");
        if (usuarioSesion == null) return "redirect:/login";

        mesaService.activar(nroMesa, usuarioSesion);
        ra.addAttribute("mMsg", "Mesa activada.");
        return dashboardMesas("mesaAct");
    }

    @GetMapping("/siguiente-nro")
    @ResponseBody
    public String siguienteNro() {
        return mesaService.generarSiguienteNro();
    }
}
