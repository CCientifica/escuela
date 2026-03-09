package com.example.demo.controller;

import com.example.demo.model.ContactoPQRS;
import com.example.demo.repository.ContactoPQRSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    private com.example.demo.repository.ClaseRepository claseRepository;

    @GetMapping({ "", "/", "/index" })
    public String index() {
        return "index";
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
    public String clasesPublicas(org.springframework.ui.Model model) {
        model.addAttribute("clases", claseRepository.findAll());
        return "clases-publicas";
    }
}
