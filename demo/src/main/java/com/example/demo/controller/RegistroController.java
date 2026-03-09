package com.example.demo.controller;

import com.example.demo.model.Estudiante;
import com.example.demo.model.Instructor;
import com.example.demo.repository.EstudianteRepository;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    // --- ESTUDIANTES ---
    @GetMapping("/estudiantes")
    public String mostrarFormularioEstudiante(Model model) {
        model.addAttribute("estudiante", new Estudiante());
        return "registro-estudiante";
    }

    @PostMapping("/estudiantes")
    public String registrarEstudiante(Estudiante estudiante, @RequestParam("password") String password,
            @RequestParam("metodoPago") String metodoPago, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            // 1. Crear Usuario para login
            Usuario nuevoUsuario = new Usuario();
            // Para el estudiante usaremos su correo electrónico como username
            nuevoUsuario.setEmail(estudiante.getCorreoElectronico());
            nuevoUsuario.setPassword(passwordEncoder.encode(password));
            nuevoUsuario.setRol("ESTUDIANTE");

            // 2. Asociarlo al Estudiante
            estudiante.setUsuario(nuevoUsuario);

            // 3. Guardar todo (Cascade = ALL se encarga de guardar Usuario)
            Estudiante guardado = estudianteRepository.save(estudiante);
            redirectAttributes.addFlashAttribute("mensaje",
                    "¡Estudiante pre-inscrito! Por favor realiza el pago inicial para confirmar la matrícula.");
            redirectAttributes.addFlashAttribute("metodoPago", metodoPago);
            return "redirect:/pagos/checkout/" + guardado.getId();
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("mensajeError", "Error al registrar: verifique que el documento no esté duplicado.");
            return "registro-estudiante";
        }
    }

    // --- INSTRUCTORES ---
    @GetMapping("/instructor")
    public String mostrarFormularioInstructor(Model model) {
        model.addAttribute("instructor", new Instructor());
        return "registro-instructor";
    }

    @PostMapping("/instructor")
    public String registrarInstructor(Instructor instructor, @RequestParam("password") String password, Model model) {
        try {
            // 1. Crear Usuario para login
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setEmail(instructor.getCorreoElectronico());
            nuevoUsuario.setPassword(passwordEncoder.encode(password));
            nuevoUsuario.setRol("INSTRUCTOR");
            nuevoUsuario.setActivo(false); // Requiere aprobación del Administrador

            // 2. Asociarlo al Instructor
            instructor.setUsuario(nuevoUsuario);

            // 3. Guardar todo
            instructorRepository.save(instructor);
            model.addAttribute("mensajeExito", "¡El instructor ha sido registrado con éxito!");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("mensajeError", "Error al registrar: el correo podría ya estar en uso.");
        }
        return "registro-instructor";
    }
}
