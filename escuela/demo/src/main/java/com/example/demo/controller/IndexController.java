package com.example.demo.controller;

import com.example.demo.model.ContactoPQRS;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private ContactoPQRSRepository contactoPQRSRepository;

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private NoticiaRepository noticiaRepository;

    @Autowired
    private TestimonioRepository testimonioRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @GetMapping({ "", "/", "/index" })
    public String index(Model model) {
        // Contenido dinámico desde la base de datos
        model.addAttribute("noticias", noticiaRepository.findAllByOrderByFechaPublicacionDesc());
        model.addAttribute("testimonios", testimonioRepository.findAllByOrderByEstrellasDesc());
        model.addAttribute("eventos", eventoRepository.findAllByOrderByFechaEventoAsc());
        return "index";
    }

    @PostMapping("/testimonio/nuevo")
    public String guardarTestimonio(@ModelAttribute com.example.demo.model.Testimonio testimonio, RedirectAttributes redirectAttributes) {
        testimonioRepository.save(testimonio);
        redirectAttributes.addAttribute("mensajeExito", "¡Muchas gracias por tu testimonio! Ha sido publicado exitosamente.");
        return "redirect:/index";
    }

    @PostMapping("/contacto")
    public String guardarContacto(@ModelAttribute ContactoPQRS contacto, RedirectAttributes redirectAttributes) {
        contactoPQRSRepository.save(contacto);
        redirectAttributes.addAttribute("mensajeExito",
                "Hemos recibido tus datos correctamente. Nuestro equipo se pondrá en contacto contigo muy pronto.");
        return "redirect:/index";
    }

    @GetMapping({ "/corporativo", "/mision", "/vision", "/valores" })
    public String corporativo() {
        return "corporativo";
    }

    @GetMapping({ "/servicios", "/eventos", "/servicios-eventos" })
    public String serviciosEventos() {
        return "servicios-eventos";
    }

    @GetMapping("/clases-publicas")
    public String clasesPublicas(Model model) {
        model.addAttribute("clases", claseRepository.findAll());
        return "clases-publicas";
    }
}
