package com.example.demo.controller;

import com.example.demo.model.Estudiante;
import com.example.demo.model.Pago;
import com.example.demo.repository.EstudianteRepository;
import com.example.demo.repository.PagoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        model.addAttribute("pagos", pagos);
        model.addAttribute("nuevoPago", new Pago());
        return "pagos/list";
    }

    @GetMapping("/checkout/{estudianteId}")
    public String mostrarCheckout(@PathVariable Long estudianteId, Model model, RedirectAttributes redirectAttributes) {
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(estudianteId);

        if (estudianteOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "El estudiante especificado no existe para cobro.");
            return "redirect:/login";
        }

        Pago nuevoPago = new Pago();
        nuevoPago.setEstudiante(estudianteOpt.get());

        if (model.containsAttribute("metodoPago")) {
            nuevoPago.setMetodoPago((String) model.getAttribute("metodoPago"));
        }

        model.addAttribute("pago", nuevoPago);
        model.addAttribute("estudiante", estudianteOpt.get());

        return "pagos/checkout";
    }

    @PostMapping("/checkout/procesar")
    public String procesarCheckout(@Valid @ModelAttribute("pago") Pago pago,
            BindingResult bindingResult,
            @RequestParam("estudianteId") Long estudianteId,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            Estudiante estudiante = estudianteRepository.findById(estudianteId).orElse(null);
            model.addAttribute("estudiante", estudiante);
            return "pagos/checkout";
        }

        try {
            Estudiante estudiante = estudianteRepository.findById(estudianteId)
                    .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));

            pago.setEstudiante(estudiante);

            if (pago.getFechaPago() == null) {
                pago.setFechaPago(LocalDate.now());
            }

            pagoRepository.save(pago);

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
            return "redirect:/pagos/checkout/" + estudianteId;
        }

        return "redirect:/login?registered=true";
    }
}

