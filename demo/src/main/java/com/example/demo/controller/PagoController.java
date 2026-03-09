package com.example.demo.controller;

import com.example.demo.model.Estudiante;
import com.example.demo.model.Pago;
import com.example.demo.repository.EstudianteRepository;
import com.example.demo.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @GetMapping
    public String listarPagos(Model model) {
        List<Pago> pagos = pagoRepository.findAll();
        // List<Estudiante> estudiantes = estudianteRepository.findAll(); // This line
        // is no longer needed for the modal

        model.addAttribute("pagos", pagos);
        // Los estudiantes ya no se necesitan para el modal porque es read-only la vista
        model.addAttribute("nuevoPago", new Pago());

        return "pagos/list";
    }

    // --- NUEVO FLUJO DE CHECKOUT ---
    @GetMapping("/checkout/{estudianteId}")
    public String mostrarCheckout(@PathVariable Long estudianteId, Model model, RedirectAttributes redirectAttributes) {

        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(estudianteId);

        if (estudianteOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "El estudiante especificado no existe para cobro.");
            return "redirect:/estudiantes";
        }

        Pago nuevoPago = new Pago();
        nuevoPago.setEstudiante(estudianteOpt.get());

        // Si viene un metodoPago de la redirección del registro, lo preseleccionamos
        if (model.containsAttribute("metodoPago")) {
            nuevoPago.setMetodoPago((String) model.getAttribute("metodoPago"));
        }

        model.addAttribute("pago", nuevoPago);
        model.addAttribute("estudiante", estudianteOpt.get());

        return "pagos/checkout";
    }

    @PostMapping("/checkout/procesar")
    public String procesarCheckout(@ModelAttribute("pago") Pago pago,
            @RequestParam("estudianteId") Long estudianteId,
            RedirectAttributes redirectAttributes) {
        try {
            Estudiante estudiante = estudianteRepository.findById(estudianteId)
                    .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));

            pago.setEstudiante(estudiante);

            // Si no se envía fecha, usar la de hoy
            if (pago.getFechaPago() == null) {
                pago.setFechaPago(LocalDate.now());
            }

            pagoRepository.save(pago);

            // Actualizar estado general del estudiante si el pago impacta su cartera
            if ("PAZ_Y_SALVO".equals(pago.getEstado()) || "Pagado".equals(pago.getEstado())) {
                estudiante.setEstadoPago("Al día");
                estudianteRepository.save(estudiante);
            } else if ("Vencido".equals(pago.getEstado())) {
                estudiante.setEstadoPago("Pendiente");
                estudianteRepository.save(estudiante);
            }

            redirectAttributes.addFlashAttribute("mensaje", "¡Inscripción confirmada! El pago inicial de "
                    + estudiante.getNombreCompleto() + " fue recibido correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al registrar el pago: " + e.getMessage());
        }

        return "redirect:/estudiantes"; // Tras cobrar redirige a la lista activa general
    }
}
