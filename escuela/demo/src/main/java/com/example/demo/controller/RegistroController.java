package com.example.demo.controller;

import com.example.demo.model.Estudiante;
import com.example.demo.model.Instructor;
import com.example.demo.repository.EstudianteRepository;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/registro")
public class RegistroController {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/estudiantes")
    public String mostrarFormularioEstudiante(Model model) {
        if (!model.containsAttribute("estudiante")) {
            model.addAttribute("estudiante", new Estudiante());
        }
        return "registro-estudiante";
    }

    @PostMapping("/estudiantes")
    public String registrarEstudiante(@Valid @ModelAttribute("estudiante") Estudiante estudiante,
            BindingResult bindingResult,
            @RequestParam("password") String password,
            @RequestParam("metodoPago") String metodoPago,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "registro-estudiante";
        }

        if (password == null || password.length() < 6) {
            model.addAttribute("mensajeError", "La contraseña debe tener al menos 6 caracteres.");
            return "registro-estudiante";
        }

        try {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setEmail(estudiante.getCorreoElectronico());
            nuevoUsuario.setPassword(passwordEncoder.encode(password));
            nuevoUsuario.setRol("ESTUDIANTE");

            estudiante.setUsuario(nuevoUsuario);

            Estudiante guardado = estudianteRepository.save(estudiante);
            redirectAttributes.addFlashAttribute("mensaje",
                    "¡Estudiante pre-inscrito! Por favor realiza el pago inicial para confirmar la matrícula.");
            redirectAttributes.addFlashAttribute("metodoPago", metodoPago);
            return "redirect:/pagos/checkout/" + guardado.getId();
        } catch (Exception e) {
            model.addAttribute("mensajeError", "Error al registrar: verifique que el documento o correo no estén duplicados.");
            return "registro-estudiante";
        }
    }

    @GetMapping("/instructor")
    public String mostrarFormularioInstructor(Model model) {
        if (!model.containsAttribute("instructor")) {
            model.addAttribute("instructor", new Instructor());
        }
        return "registro-instructor";
    }

    @PostMapping("/instructor")
    public String registrarInstructor(@Valid @ModelAttribute("instructor") Instructor instructor,
            BindingResult bindingResult,
            @RequestParam("password") String password, Model model) {

        if (bindingResult.hasErrors()) {
            return "registro-instructor";
        }

        try {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setEmail(instructor.getCorreoElectronico());
            nuevoUsuario.setPassword(passwordEncoder.encode(password));
            nuevoUsuario.setRol("INSTRUCTOR");
            nuevoUsuario.setActivo(false);

            instructor.setUsuario(nuevoUsuario);

            instructorRepository.save(instructor);
            model.addAttribute("mensajeExito", "¡El instructor ha sido registrado con éxito! Pendiente de aprobación.");
        } catch (Exception e) {
            model.addAttribute("mensajeError", "Error al registrar: el correo podría ya estar en uso.");
        }
        return "registro-instructor";
    }
}
