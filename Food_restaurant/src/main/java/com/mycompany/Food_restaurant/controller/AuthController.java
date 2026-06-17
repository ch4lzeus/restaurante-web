package com.mycompany.Food_restaurant.controller;

import com.mycompany.Food_restaurant.service.UsuarioService;
import com.mycompany.Food_restaurant.service.CatalogoService;
import com.mycompany.Food_restaurant.service.MesaService;
import com.mycompany.Food_restaurant.service.ReservaService;
import com.mycompany.Food_restaurant.service.PedidoService;
import com.mycompany.Food_restaurant.config.ImageUploadPaths;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.mycompany.Food_restaurant.service.PagoService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

/*
 *Recibe y responde solicitudes web.
 *Parecido a un Servlet, pero más limpio y con anotaciones.
 */
@Controller
public class AuthController {

    private final UsuarioService usuarioService;
    private final CatalogoService catalogoService;
    private final MesaService mesaService;
    private final ReservaService reservaService;
    private final PedidoService pedidoService;
    private final PagoService pagoService;

    // Carpeta donde se guardan las imagenes de platos durante desarrollo.
    private static final Path UPLOAD_DIR = ImageUploadPaths.platosDir();

    public AuthController(UsuarioService usuarioService, CatalogoService catalogoService,
                          MesaService mesaService, ReservaService reservaService,
                          PedidoService pedidoService, PagoService pagoService) {
        this.usuarioService = usuarioService;
        this.catalogoService = catalogoService;
        this.mesaService = mesaService;
        this.reservaService = reservaService;
        this.pedidoService = pedidoService;
        this.pagoService = pagoService;
    }

    @ModelAttribute
    public void noCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
    }

    // Metodo auxiliar para guardar imagen y retornar la ruta web
    private String guardarImagen(MultipartFile imagen, String rutaActual) {
        if (imagen == null || imagen.isEmpty()) return rutaActual;
        try {
            String nombreOriginal = imagen.getOriginalFilename();
            String ext = obtenerExtensionImagen(nombreOriginal, imagen.getContentType());

            String nombreArchivo = UUID.randomUUID() + ext;
            Path destino = UPLOAD_DIR.resolve(nombreArchivo).normalize();

            Files.createDirectories(UPLOAD_DIR);
            Files.copy(imagen.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Imagen guardada en: " + destino);
            return "/img/platos/" + nombreArchivo;
        } catch (IOException e) {
            e.printStackTrace();
            return rutaActual;
        }
    }

    private String obtenerExtensionImagen(String nombreOriginal, String contentType) {
        if (nombreOriginal != null && nombreOriginal.contains(".")) {
            String ext = nombreOriginal.substring(nombreOriginal.lastIndexOf(".")).toLowerCase();
            if (ext.matches("\\.(jpg|jpeg|png|gif|webp|bmp|svg|avif)")) {
                return ext;
            }
        }

        if (contentType != null && contentType.startsWith("image/")) {
            return switch (contentType) {
                case "image/png" -> ".png";
                case "image/gif" -> ".gif";
                case "image/webp" -> ".webp";
                case "image/bmp" -> ".bmp";
                case "image/svg+xml" -> ".svg";
                case "image/avif" -> ".avif";
                default -> ".jpg";
            };
        }

        return ".jpg";
    }

    // Mostrar login al abrir el proyecto
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuarioCodigo");
        if (usuario != null) {
            return esCliente(session) ? "redirect:/cliente" : "redirect:/dashboard";
        }
        model.addAttribute("platosData", catalogoService.listarAlimentosActivos());
        return "cliente/index";
    }

    @GetMapping("/login")
    public String loginPage2() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {

        Map<String, Object> result = usuarioService.login(username, password);
        String estado = (String) result.get("estado");

        switch (estado) {
            case "OK":
                session.setAttribute("usuarioCodigo", result.get("CodUsuario"));
                session.setAttribute("usuarioNombre", result.get("Nombre"));
                session.setAttribute("usuarioRol", result.get("CodRol"));
                model.addAttribute("LOGIN", "ok");
                model.addAttribute("ERROR", "null");
                model.addAttribute("REDIRECT_URL", "CLI".equals(result.get("CodRol")) ? "/cliente" : "/dashboard");
                break;
            case "ERROR_USER":
            case "ERROR_PASS":
                model.addAttribute("LOGIN", "null");
                model.addAttribute("ERROR", estado);
                break;
            case "INACTIVO":
                model.addAttribute("LOGIN", "null");
                model.addAttribute("ERROR", estado);
                break;
            default:
                model.addAttribute("LOGIN", "null");
                model.addAttribute("ERROR", "ERROR");
        }

        return "login";
    }

    // Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (session.getAttribute("usuarioCodigo") == null) {
            return "redirect:/login";
        }
        if (esCliente(session)) {
            return "redirect:/cliente";
        }
        model.addAttribute("listaUsuarios", usuarioService.listarUsuarios());
        model.addAttribute("listaCategorias", catalogoService.listarCategorias());
        model.addAttribute("listaAlimentos", catalogoService.listarAlimentos());
        model.addAttribute("listaMesas", mesaService.listarTodas());
        model.addAttribute("listaMesasAct", mesaService.listarActivas());
        model.addAttribute("listaReservas", reservaService.listarTodas());
        model.addAttribute("listaClientes", reservaService.listarClientes());
        model.addAttribute("listaPedidos", pedidoService.listarOperativos());
        model.addAttribute("listaPedidosCocina", pedidoService.listarCocina());
        model.addAttribute("listaPedidosDelivery", pedidoService.listarDeliveryOperativos());
        model.addAttribute("cntPendientes", pedidoService.contarPendientes());
        model.addAttribute("cntEnCocina", pedidoService.contarEnCocina());
        model.addAttribute("cntListos", pedidoService.contarListos());
        model.addAttribute("cntDelivery", pedidoService.contarDelivery());


        model.addAttribute("cntPedidosHoy", pedidoService.contarPedidosHoy());
        model.addAttribute("totalCobradoHoy", pagoService.totalCobradoHoy());


        return "dashboard";
    }

    private boolean esCliente(HttpSession session) {
        return "CLI".equals(session.getAttribute("usuarioRol"));
    }

    private boolean esAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("usuarioRol"));
    }

    private String dashboard(String sec, String tab, String msg) {
        String url = "redirect:/dashboard?sec=" + sec;
        if (tab != null && !tab.isBlank()) url += "&tab=" + tab;
        if (msg != null && !msg.isBlank()) url += "&msg=" + msg;
        return url;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/session-status")
    @ResponseBody
    public Map<String, Object> sessionStatus(HttpSession session) {
        Object usuario = session.getAttribute("usuarioCodigo");
        Object rol = session.getAttribute("usuarioRol");
        return Map.of(
                "logged", usuario != null,
                "rol", rol != null ? rol : ""
        );
    }

    @PostMapping("/usuarios/registrar")
    public String registrarUsuarioAdministrativo(@RequestParam String codUsuario,
                                                 @RequestParam String dni,
                                                 @RequestParam String nombre,
                                                 @RequestParam String codRol,
                                                 @RequestParam String contrasena,
                                                 @RequestParam String confirmar,
                                                 HttpSession session) {
        if (!esAdmin(session)) return "redirect:/dashboard";
        if (!contrasena.equals(confirmar)) return dashboard("usuarios", "nuevo", "usrErr");
        boolean registrado = usuarioService.registrarUsuarioAdministrativo(codUsuario, dni, nombre, codRol, contrasena);
        return registrado ? dashboard("usuarios", "nuevo", "usrOk") : dashboard("usuarios", "nuevo", "usrDup");
    }

    @PostMapping("/usuarios/editar")
    public String editarUsuario(@RequestParam String codUsuario,
                                @RequestParam String dni,
                                @RequestParam String nombre,
                                @RequestParam String codRol,
                                @RequestParam(required = false) String contrasena,
                                HttpSession session) {
        if (!esAdmin(session)) return "redirect:/dashboard";
        boolean editado = usuarioService.editarUsuario(codUsuario, dni, nombre, codRol, contrasena);
        return editado ? dashboard("usuarios", "lista", "usrEdit") : dashboard("usuarios", "lista", "usrErr");
    }

    @GetMapping("/usuarios/desactivar")
    public String desactivarUsuario(@RequestParam String cod, HttpSession session) {
        if (!esAdmin(session)) return "redirect:/dashboard";
        boolean desactivado = usuarioService.desactivarUsuario(cod);
        return desactivado ? dashboard("usuarios", "lista", "usrDes") : dashboard("usuarios", "lista", "usrErr");
    }

    @GetMapping("/usuarios/activar")
    public String activarUsuario(@RequestParam String cod, HttpSession session) {
        if (!esAdmin(session)) return "redirect:/dashboard";
        boolean activado = usuarioService.activarUsuario(cod);
        return activado ? dashboard("usuarios", "lista", "usrAct") : dashboard("usuarios", "lista", "usrErr");
    }

    @PostMapping("/categorias/registrar")
    public String registrarCategoria(@RequestParam String codCategoria,
                                     @RequestParam String nombre,
                                     HttpSession session) {
        if (!esAdmin(session)) return "redirect:/dashboard";
        String usuario = String.valueOf(session.getAttribute("usuarioCodigo"));
        boolean registrado = catalogoService.registrarCategoria(codCategoria, nombre, usuario);
        return registrado ? dashboard("cocina", "categorias", "catOk") : dashboard("cocina", "categorias", "catDup");
    }

    @PostMapping("/alimentos/registrar")
    public String registrarAlimento(@RequestParam String codAlimento,
                                    @RequestParam String codCategoria,
                                    @RequestParam String nombre,
                                    @RequestParam(required = false) String descripcion,
                                    @RequestParam(value = "imagen", required = false) MultipartFile imagen,
                                    @RequestParam(defaultValue = "0") double descuentoPromocion,
                                    @RequestParam double precio,
                                    HttpSession session) {
        if (!esAdmin(session)) return "redirect:/dashboard";
        String usuario = String.valueOf(session.getAttribute("usuarioCodigo"));
        String rutaImagen = guardarImagen(imagen, null);
        boolean registrado = catalogoService.registrarAlimento(codAlimento, codCategoria, nombre, descripcion,
                rutaImagen, descuentoPromocion, precio, usuario);
        return registrado ? dashboard("cocina", "gestionar", "aliOk") : dashboard("cocina", "gestionar", "aliDup");
    }

    @PostMapping("/alimentos/editar")
    public String editarAlimento(@RequestParam String codAlimento,
                                 @RequestParam String codCategoria,
                                 @RequestParam String nombre,
                                 @RequestParam(required = false) String descripcion,
                                 @RequestParam(value = "imagen", required = false) MultipartFile imagen,
                                 @RequestParam(required = false) String rutaImagenActual,
                                 @RequestParam(defaultValue = "0") double descuentoPromocion,
                                 @RequestParam double precio,
                                 @RequestParam String estado,
                                 HttpSession session) {
        if (!esAdmin(session)) return "redirect:/dashboard";
        String usuario = String.valueOf(session.getAttribute("usuarioCodigo"));
        // Si no sube imagen nueva conserva la actual
        String rutaImagen = guardarImagen(imagen, rutaImagenActual);
        boolean editado = catalogoService.editarAlimento(codAlimento, codCategoria, nombre, descripcion,
                rutaImagen, descuentoPromocion, precio, estado, usuario);
        return editado ? dashboard("cocina", "alimentos", "aliEdit") : dashboard("cocina", "alimentos", "aliErr");
    }

    @GetMapping("/alimentos/desactivar")
    public String desactivarAlimento(@RequestParam String cod, HttpSession session) {
        if (!esAdmin(session)) return "redirect:/dashboard";
        String usuario = String.valueOf(session.getAttribute("usuarioCodigo"));
        boolean desactivado = catalogoService.desactivarAlimento(cod, usuario);
        return desactivado ? dashboard("cocina", "alimentos", "aliDes") : dashboard("cocina", "alimentos", "aliErr");
    }

    @GetMapping("/alimentos/activar")
    public String activarAlimento(@RequestParam String cod, HttpSession session) {
        if (!esAdmin(session)) return "redirect:/dashboard";
        String usuario = String.valueOf(session.getAttribute("usuarioCodigo"));
        boolean activado = catalogoService.activarAlimento(cod, usuario);
        return activado ? dashboard("cocina", "alimentos", "aliAct") : dashboard("cocina", "alimentos", "aliErr");
    }

    @GetMapping("/registro")
    public String registroPage() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam String codUsuario,
                                   @RequestParam String dni,
                                   @RequestParam String nombre,
                                   @RequestParam String contrasena,
                                   @RequestParam String confirmar,
                                   Model model) {
        if (!contrasena.equals(confirmar)) {
            model.addAttribute("MSG", "noMatch");
            return "registro";
        }
        boolean registrado = usuarioService.registrarUsuario(codUsuario, dni, nombre, contrasena);
        model.addAttribute("MSG", registrado ? "ok" : "dup");
        return "registro";
    }

    @GetMapping("/recuperar")
    public String recuperarPage() {
        return "recuperar";
    }

    @PostMapping("/recuperar")
    public String recuperarPassword(@RequestParam String dni,
                                    @RequestParam String nuevaPass,
                                    @RequestParam String confirmar,
                                    Model model) {
        if (!nuevaPass.equals(confirmar)) {
            model.addAttribute("MSG", "errR");
            return "recuperar";
        }
        boolean actualizado = usuarioService.actualizarPasswordPorDni(dni, nuevaPass);
        model.addAttribute("MSG", actualizado ? "pass" : "errR");
        return "recuperar";
    }
}
