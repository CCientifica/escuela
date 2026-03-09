package com.example.demo.controller;

import com.example.demo.model.Estudiante;
import com.example.demo.service.EscuelaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EscuelaController {

    @Autowired
    private EscuelaService escuelaService;

    // --- ESTUDIANTES ---
    @GetMapping("/estudiantes")
    public String listarEstudiantes(Model model) {
        model.addAttribute("estudiantes", escuelaService.listarEstudiantes());
        return "estudiantes/list";
    }

    @GetMapping("/estudiantes/nuevo")
    public String mostrarFormularioEstudiante(Model model) {
        model.addAttribute("estudiante", new Estudiante());
        return "estudiantes/form";
    }

    @PostMapping("/estudiantes/guardar")
    public String guardarEstudiante(@ModelAttribute("estudiante") Estudiante estudiante) {
        escuelaService.guardarEstudiante(estudiante);
        return "redirect:/estudiantes";
    }

    @GetMapping("/estudiantes/eliminar/{id}")
    public String eliminarEstudiante(@PathVariable Long id) {
        escuelaService.eliminarEstudiante(id);
        return "redirect:/estudiantes";
    }
}
