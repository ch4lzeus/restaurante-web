package com.mycompany.Food_restaurant.controller;

import com.mycompany.Food_restaurant.service.CatalogoService;
import com.mycompany.Food_restaurant.repository.ClienteRepository;
import com.mycompany.Food_restaurant.entity.Cliente;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ClienteController {

    private final CatalogoService catalogoService;
    private final ClienteRepository clienteRepository;

    public ClienteController(CatalogoService catalogoService, ClienteRepository clienteRepository) {
        this.catalogoService = catalogoService;
        this.clienteRepository = clienteRepository;
    }

    @ModelAttribute
    public void noCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
    }

    @GetMapping("/cliente")
    public String index(Model model, HttpSession session) {
        model.addAttribute("platosData", catalogoService.listarAlimentosActivos());
        setClienteCodigo(model, session);
        return "cliente/index";
    }

    @GetMapping("/cliente/menu")
    public String menu(Model model, HttpSession session) {
        model.addAttribute("platosData", catalogoService.listarAlimentosActivos());
        setClienteCodigo(model, session);
        return "cliente/menu";
    }

    @GetMapping("/cliente/plato/{id}")
    public String detallePlato(@PathVariable String id, Model model, HttpSession session) {
        model.addAttribute("platosData", catalogoService.listarAlimentosActivos());
        model.addAttribute("platoDetalle", catalogoService.obtenerAlimentoActivo(id));
        setClienteCodigo(model, session);
        return "cliente/detalle-plato";
    }

    @GetMapping("/cliente/carrito")
    public String carrito(Model model, HttpSession session) {
        model.addAttribute("platosData", catalogoService.listarAlimentosActivos());
        setClienteCodigo(model, session);
        return "cliente/carrito";
    }

    private void setClienteCodigo(Model model, HttpSession session) {
        String codUsuario = (String) session.getAttribute("usuarioCodigo");
        String codCliente = "";
        if (codUsuario != null) {
            Cliente cliente = clienteRepository.findByCodUsuario(codUsuario).orElse(null);
            if (cliente != null) {
                codCliente = cliente.getCodCliente();
            }
        }
        model.addAttribute("clienteCod", codCliente);
    }
}
