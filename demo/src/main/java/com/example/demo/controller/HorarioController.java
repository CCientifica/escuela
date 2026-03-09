package com.example.demo.controller;

import com.example.demo.model.AsignacionClase;
import com.example.demo.model.Escenario;
import com.example.demo.model.Instructor;
import com.example.demo.model.PlanEntrenamiento;
import com.example.demo.repository.AsignacionClaseRepository;
import com.example.demo.repository.EscenarioRepository;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.repository.PlanEntrenamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/horarios")
public class HorarioController {

    @Autowired
    private AsignacionClaseRepository asignacionRepository;

    @Autowired
    private PlanEntrenamientoRepository planRepository;

    @Autowired
    private EscenarioRepository escenarioRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @GetMapping("/estudiantes")
    public String dashboardHorarios(Model model) {

        // Ensure some base data exists if the DB is empty just for visualization/demo
        // purposes
        if (planRepository.count() == 0) {
            planRepository.save(new PlanEntrenamiento(null, "Plan Semilleros (Iniciación)",
                    "Equilibrio, frenado básico y confianza.", "2 sesiones por semana"));
            planRepository.save(new PlanEntrenamiento(null, "Plan Formativo (Intermedio)",
                    "Técnica de empuje, cruce de curvas y resistencia.", "3 sesiones por semana"));
            planRepository.save(new PlanEntrenamiento(null, "Plan Élite (Competencia)",
                    "Velocidad máxima, táctica de carrera y preparación regional.", "5 sesiones por semana"));
        }

        if (escenarioRepository.count() == 0) {
            escenarioRepository
                    .save(new Escenario(null, "Patinódromo de la Alegría", "Pista de Velocidad (Anillo)", 25));
            escenarioRepository.save(new Escenario(null, "Polideportivo Sur", "Cancha de Hockey (Plana)", 15));
            escenarioRepository.save(new Escenario(null, "Playas de Pozos Colorados", "Zona Peatonal (Ruta)", 50));
        }

        List<PlanEntrenamiento> planes = planRepository.findAll();
        List<Escenario> escenarios = escenarioRepository.findAll();
        List<Instructor> instructores = instructorRepository.findAll();
        List<AsignacionClase> asignaciones = asignacionRepository.findAll();

        model.addAttribute("planes", planes);
        model.addAttribute("escenarios", escenarios);
        model.addAttribute("instructores", instructores);
        model.addAttribute("asignaciones", asignaciones);

        return "horarios/list";
    }

    @PostMapping("/crear")
    public String crearAsignacion(@RequestParam("diaSemana") String diaSemana,
            @RequestParam("franjaHoraria") String franjaHoraria,
            @RequestParam("planId") Long planId,
            @RequestParam("instructorId") Long instructorId,
            @RequestParam("escenarioId") Long escenarioId,
            RedirectAttributes redirectAttributes) {

        try {
            // 1. Validar la prevención de conflictos (Mismo escenario en misma franja y
            // día)
            Optional<AsignacionClase> conflictoEspacial = asignacionRepository
                    .findByDiaSemanaAndFranjaHorariaAndEscenarioId(diaSemana, franjaHoraria, escenarioId);

            if (conflictoEspacial.isPresent()) {
                redirectAttributes.addFlashAttribute("mensajeError",
                        "¡Conflicto de Escenario! La pista seleccionada ya está ocupada el " + diaSemana
                                + " en el horario de " + franjaHoraria + ".");
                return "redirect:/horarios/estudiantes";
            }

            // 2. Traer las entidades seleccionadas
            PlanEntrenamiento plan = planRepository.findById(planId)
                    .orElseThrow(() -> new IllegalArgumentException("Plan inváido"));
            Instructor instructor = instructorRepository.findById(instructorId)
                    .orElseThrow(() -> new IllegalArgumentException("Instructor inváido"));
            Escenario escenario = escenarioRepository.findById(escenarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Escenario inváido"));

            // 3. Crear y Guardar
            AsignacionClase nuevaClase = new AsignacionClase(null, diaSemana, franjaHoraria, plan, instructor,
                    escenario);
            asignacionRepository.save(nuevaClase);

            redirectAttributes.addFlashAttribute("mensajeExito",
                    "El bloque de entrenamiento ha sido asignado con éxito en el Calendario Maestro.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error procesando la solicitud: " + e.getMessage());
        }

        return "redirect:/horarios/estudiantes";
    }
}
