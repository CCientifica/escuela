package com.example.demo.controller;

import com.example.demo.model.Clase;
import com.example.demo.model.Estudiante;
import com.example.demo.model.Instructor;
import com.example.demo.model.Pago;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ClaseRepository;
import com.example.demo.repository.EstudianteRepository;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.repository.PagoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.ValoracionInstructorRepository;
import com.example.demo.repository.AsistenciaRepository;
import com.example.demo.repository.CalificacionRepository;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.model.Asistencia;
import com.example.demo.model.Calificacion;
import com.example.demo.model.ValoracionInstructor;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/panel")
public class DashboardController {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private ValoracionInstructorRepository valoracionInstructorRepository;

    @Autowired
    private com.example.demo.repository.ContactoPQRSRepository contactoPQRSRepository;

    @GetMapping("/estudiante")
    public String showEstudianteDashboard(HttpSession session, Model model) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Estudiante> estudianteOpt = estudianteRepository.findByUsuarioEmail(email);

        if (estudianteOpt.isPresent()) {
            Estudiante estudiante = estudianteOpt.get();
            model.addAttribute("estudiante", estudiante);

            // Pasar un resumen para la vista principal si se desea
        }

        model.addAttribute("correo", email);
        return "dashboard/estudiante";
    }

    @GetMapping("/instructor")
    public String showInstructorDashboard(HttpSession session, Model model) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Instructor> instructorOpt = instructorRepository.findByUsuarioEmail(email);

        if (instructorOpt.isPresent()) {
            Instructor instructor = instructorOpt.get();
            List<Clase> misClases = claseRepository.findByInstructorId(instructor.getId());

            // Calcular métricas reales
            int totalAlumnos = 0;
            String proximaClaseNombre = "Sin clases asignadas";
            String proximaClaseHorario = "--:--";

            if (!misClases.isEmpty()) {
                // Total unique students
                java.util.Set<Estudiante> alumnosUnicos = new java.util.HashSet<>();
                for (Clase c : misClases) {
                    alumnosUnicos.addAll(c.getEstudiantes());
                }
                totalAlumnos = alumnosUnicos.size();

                // Simple próximo asumiendo la primera de la lista por ahora
                Clase prox = misClases.get(0);
                proximaClaseNombre = prox.getNombre() + " (" + prox.getNivel() + ")";
                proximaClaseHorario = prox.getHorario();
            }

            model.addAttribute("totalAlumnos", totalAlumnos);
            model.addAttribute("proximaClaseNombre", proximaClaseNombre);
            model.addAttribute("proximaClaseHorario", proximaClaseHorario);
            model.addAttribute("totalClases", misClases.size());

            model.addAttribute("instructor", instructor);
            model.addAttribute("clases", misClases);
            model.addAttribute("correo", email);
            return "dashboard/instructor";
        }

        return "redirect:/login";
    }

    // --- Panel del Instructor: Redireccionador Inteligente ---
    @GetMapping("/instructor/mis-clases")
    public String redireccionarPrimeraClase(HttpSession session, RedirectAttributes redirectAttributes) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Instructor> instructorOpt = instructorRepository.findByUsuarioEmail(email);

        if (instructorOpt.isPresent()) {
            List<Clase> misClases = claseRepository.findByInstructorId(instructorOpt.get().getId());
            if (!misClases.isEmpty()) {
                return "redirect:/panel/instructor/clases/" + misClases.get(0).getId();
            } else {
                redirectAttributes.addFlashAttribute("mensajeError",
                        "Aún no tienes clases asignadas para gestionar. Consulta a Administración.");
                return "redirect:/panel/instructor";
            }
        }
        return "redirect:/login";
    }

    // --- Panel del Instructor: Valoraciones Recibidas ---
    @GetMapping("/instructor/valoraciones")
    public String verValoracionesInstructor(HttpSession session, Model model) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Instructor> instructorOpt = instructorRepository.findByUsuarioEmail(email);

        if (instructorOpt.isPresent()) {
            Instructor instructor = instructorOpt.get();
            List<ValoracionInstructor> valoraciones = valoracionInstructorRepository
                    .findByInstructorId(instructor.getId());

            double promedio = 0.0;
            if (!valoraciones.isEmpty()) {
                promedio = valoraciones.stream().mapToInt(ValoracionInstructor::getEstrellas).average().orElse(0.0);
            }

            // Re-fetch classes for the sidebar generic link "Mis Alumnos/Agenda" if they
            // need to point to the first class
            List<Clase> misClases = claseRepository.findByInstructorId(instructor.getId());

            model.addAttribute("instructor", instructor);
            model.addAttribute("clases", misClases);
            model.addAttribute("valoraciones", valoraciones);
            model.addAttribute("promedio", String.format("%.1f", promedio));
            model.addAttribute("totalValoraciones", valoraciones.size());
            model.addAttribute("correo", email);

            return "dashboard/instructor-valoraciones";
        }
        return "redirect:/login";
    }

    // --- Panel del Instructor: Gestión Académica (Asistencia y Notas) ---
    @GetMapping("/instructor/clases/{id}")
    public String verClaseInstructor(@PathVariable Long id, HttpSession session, Model model) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Instructor> instructorOpt = instructorRepository.findByUsuarioEmail(email);

        if (instructorOpt.isPresent()) {
            Instructor instructor = instructorOpt.get();
            Optional<Clase> claseOpt = claseRepository.findById(id);

            // Validar que la clase exista y pertenezca a este instructor
            if (claseOpt.isPresent() && claseOpt.get().getInstructor() != null
                    && claseOpt.get().getInstructor().getId().equals(instructor.getId())) {

                List<Clase> misClases = claseRepository.findByInstructorId(instructor.getId());
                model.addAttribute("clases", misClases);
                model.addAttribute("correo", email);
                model.addAttribute("clase", claseOpt.get());
                model.addAttribute("estudiantes", claseOpt.get().getEstudiantes());
                return "dashboard/instructor-clase-gestion";
            }
        }
        return "redirect:/panel/instructor";
    }

    @PostMapping("/instructor/clases/{claseId}/asistencia")
    public String registrarAsistencia(@PathVariable Long claseId, @RequestParam Long estudianteId,
            @RequestParam Boolean presente, @RequestParam String fechaDef, HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            java.time.LocalDate fecha = java.time.LocalDate.parse(fechaDef);
            Estudiante estudiante = estudianteRepository.findById(estudianteId).orElse(null);
            Clase clase = claseRepository.findById(claseId).orElse(null);

            if (estudiante != null && clase != null) {
                Asistencia asistencia = new Asistencia(estudiante, clase, fecha, presente);
                asistenciaRepository.save(asistencia);
                redirectAttributes.addFlashAttribute("mensajeExito", "Asistencia guardada.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al procesar la asistencia.");
        }
        return "redirect:/panel/instructor/clases/" + claseId;
    }

    @PostMapping("/instructor/clases/{claseId}/notas")
    public String registrarNota(@PathVariable Long claseId, @RequestParam Long estudianteId,
            @RequestParam Double nota, @RequestParam String observacion, @RequestParam String fechaDef,
            HttpSession session, RedirectAttributes redirectAttributes) {

        try {
            java.time.LocalDate fecha = java.time.LocalDate.parse(fechaDef);
            Estudiante estudiante = estudianteRepository.findById(estudianteId).orElse(null);
            Clase clase = claseRepository.findById(claseId).orElse(null);

            if (estudiante != null && clase != null) {
                Calificacion calificacion = new Calificacion(estudiante, clase, fecha, nota, observacion);
                calificacionRepository.save(calificacion);
                redirectAttributes.addFlashAttribute("mensajeExito", "Calificación / Valoración guardada.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al guardar la nota.");
        }
        return "redirect:/panel/instructor/clases/" + claseId;
    }

    @PostMapping("/estudiante/calificar-instructor")
    public String calificarInstructor(@RequestParam Long instructorId, @RequestParam(required = false) Long claseId,
            @RequestParam Integer estrellas, @RequestParam String comentario,
            HttpSession session, RedirectAttributes redirectAttributes) {

        String rol = (String) session.getAttribute("usuarioRol");
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();

        if (rol == null || !rol.equals("ESTUDIANTE")) {
            return "redirect:/login";
        }

        try {
            Estudiante estudiante = estudianteRepository.findByUsuarioEmail(email).orElse(null);
            Instructor instructor = instructorRepository.findById(instructorId).orElse(null);
            Clase clase = claseId != null ? claseRepository.findById(claseId).orElse(null) : null;

            if (estudiante != null && instructor != null) {
                ValoracionInstructor valoracion = new ValoracionInstructor(
                        estudiante, instructor, clase, java.time.LocalDate.now(), estrellas, comentario);
                valoracionInstructorRepository.save(valoracion);
                redirectAttributes.addFlashAttribute("mensajeExito",
                        "¡Gracias! Tu calificación ha sido enviada al instructor.");
            } else {
                redirectAttributes.addFlashAttribute("mensajeError",
                        "Error al procesar la calificación. Faltan datos.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error al enviar tu reseña.");
        }

        return "redirect:/panel/estudiante/progreso";
    }

    // --- Perfil del Estudiante ---
    @GetMapping("/estudiante/perfil")
    public String editarPerfilEstudiante(HttpSession session, Model model) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Estudiante> estudianteOpt = estudianteRepository.findByUsuarioEmail(email);

        if (estudianteOpt.isPresent()) {
            model.addAttribute("estudiante", estudianteOpt.get());
            model.addAttribute("correo", email);
            return "dashboard/estudiante-perfil";
        }
        return "redirect:/login";
    }

    @Transactional
    @PostMapping("/estudiante/perfil")
    public String guardarPerfilEstudiante(Estudiante datosActualizados, HttpSession session,
            RedirectAttributes redirectAttributes) {
        String emailActual = (String) session.getAttribute("usuarioEmail");
        Optional<Estudiante> estudianteOpt = estudianteRepository.findByUsuarioEmail(emailActual);

        if (estudianteOpt.isPresent()) {
            Estudiante existente = estudianteOpt.get();

            // Check if email was changed
            String nuevoEmail = datosActualizados.getCorreoElectronico();
            if (nuevoEmail != null && !nuevoEmail.trim().isEmpty() && !nuevoEmail.equals(emailActual)) {
                // Verify if the new email is already taken in the Usuario table
                Optional<Usuario> usuarioDuplicado = usuarioRepository.findByEmail(nuevoEmail);
                if (usuarioDuplicado.isPresent()) {
                    redirectAttributes.addFlashAttribute("mensaje",
                            "Error: El correo electrónico ya está en uso por otro usuario.");
                    return "redirect:/panel/estudiante/perfil";
                }

                // Update Email on both Estudiante and its linked Usuario
                existente.setCorreoElectronico(nuevoEmail);
                if (existente.getUsuario() != null) {
                    existente.getUsuario().setEmail(nuevoEmail);
                }

                // Update active session so the user doesn't get instantly logged out
                session.setAttribute("usuarioEmail", nuevoEmail);
            }

            // Actualizar datos permitidos
            existente.setNombreCompleto(datosActualizados.getNombreCompleto());
            existente.setFechaNacimiento(datosActualizados.getFechaNacimiento());
            existente.setGenero(datosActualizados.getGenero());
            existente.setTelefono(datosActualizados.getTelefono());
            existente.setEps(datosActualizados.getEps());
            existente.setContactoEmergencia(datosActualizados.getContactoEmergencia());

            estudianteRepository.save(existente);
            redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado correctamente.");
        }
        return "redirect:/panel/estudiante/perfil";
    }

    // --- Pagos del Estudiante ---
    @GetMapping("/estudiante/pagos")
    public String verPagosEstudiante(HttpSession session, Model model) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Estudiante> estudianteOpt = estudianteRepository.findByUsuarioEmail(email);

        if (estudianteOpt.isPresent()) {
            Estudiante estudiante = estudianteOpt.get();
            List<Pago> misPagos = pagoRepository.findByEstudianteId(estudiante.getId());
            model.addAttribute("estudiante", estudiante);
            model.addAttribute("pagos", misPagos);
            model.addAttribute("correo", email);
            return "dashboard/estudiante-pagos";
        }
        return "redirect:/login";
    }

    // --- Horarios del Estudiante ---
    @GetMapping("/estudiante/horarios")
    public String verHorariosEstudiante(HttpSession session, Model model) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Estudiante> estudianteOpt = estudianteRepository.findByUsuarioEmail(email);

        if (estudianteOpt.isPresent()) {
            Estudiante estudiante = estudianteOpt.get();
            model.addAttribute("estudiante", estudiante);
            model.addAttribute("correo", email);

            // Fetch classes, attendance, and grades for the student
            List<Clase> misClases = claseRepository.findByEstudiantesId(estudiante.getId());
            List<Asistencia> asistencias = asistenciaRepository.findByEstudianteId(estudiante.getId());
            List<Calificacion> calificaciones = calificacionRepository.findByEstudianteId(estudiante.getId());
            List<Clase> todasLasClases = claseRepository.findAll();

            model.addAttribute("clases", misClases);
            model.addAttribute("asistencias", asistencias);
            model.addAttribute("calificaciones", calificaciones);
            model.addAttribute("todasLasClases", todasLasClases);

            return "dashboard/estudiante-horarios";
        }
        return "redirect:/login";
    }

    @Transactional
    @PostMapping("/estudiante/clases/{claseId}/matricular")
    public String matricularClaseEstudiante(@PathVariable Long claseId, HttpSession session,
            RedirectAttributes redirectAttributes) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        if (email == null) {
            return "redirect:/login";
        }

        Optional<Estudiante> estudianteOpt = estudianteRepository.findByUsuarioEmail(email);
        Optional<Clase> claseOpt = claseRepository.findById(claseId);

        if (estudianteOpt.isPresent() && claseOpt.isPresent()) {
            Estudiante estudiante = estudianteOpt.get();
            Clase clase = claseOpt.get();

            if (!clase.getEstudiantes().contains(estudiante)) {
                clase.getEstudiantes().add(estudiante);
                claseRepository.save(clase);
                redirectAttributes.addFlashAttribute("mensajeExito",
                        "¡Inscripción exitosa a la clase " + clase.getNombre() + "!");
            } else {
                redirectAttributes.addFlashAttribute("mensajeError", "Ya estás inscrito en esta clase.");
            }
        }
        return "redirect:/panel/estudiante/horarios";
    }

    // --- Progreso del Estudiante y Calificaciones ---
    @GetMapping("/estudiante/progreso")
    public String verProgresoEstudiante(HttpSession session, Model model) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Estudiante> estudianteOpt = estudianteRepository.findByUsuarioEmail(email);

        if (estudianteOpt.isPresent()) {
            Estudiante estudiante = estudianteOpt.get();
            model.addAttribute("estudiante", estudiante);
            model.addAttribute("correo", email);

            List<Clase> misClases = claseRepository.findByEstudiantesId(estudiante.getId());
            List<Asistencia> asistencias = asistenciaRepository.findByEstudianteId(estudiante.getId());
            List<Calificacion> calificaciones = calificacionRepository.findByEstudianteId(estudiante.getId());

            model.addAttribute("clases", misClases);
            model.addAttribute("asistencias", asistencias);
            model.addAttribute("calificaciones", calificaciones);

            return "dashboard/estudiante-progreso";
        }
        return "redirect:/login";
    }

    // --- Panel de Administrador: Gestión de Instructores ---
    @GetMapping("/admin/instructores")
    public String listarInstructoresAdmin(HttpSession session, Model model) {

        List<Instructor> instructores = instructorRepository.findAll();
        model.addAttribute("instructores", instructores);
        return "dashboard/admin-instructores";
    }

    @GetMapping("/admin/instructores/{id}")
    public String detalleInstructorAdmin(@PathVariable Long id, HttpSession session, Model model) {

        Optional<Instructor> instructorOpt = instructorRepository.findById(id);
        if (instructorOpt.isPresent()) {
            Instructor instructor = instructorOpt.get();
            List<Clase> clases = claseRepository.findByInstructorId(instructor.getId());

            // Calcular Promedio de Valoraciones
            double promedio = 0.0;
            int totalEvals = 0;
            for (Clase c : clases) {
                List<Calificacion> calificacionesClase = calificacionRepository.findByClaseId(c.getId());
                for (Calificacion cal : calificacionesClase) {
                    promedio += cal.getNota();
                    totalEvals++;
                }
            }
            if (totalEvals > 0) {
                promedio = promedio / totalEvals;
            }

            model.addAttribute("instructor", instructor);
            model.addAttribute("clases", clases);
            model.addAttribute("promedioValoracion", String.format("%.1f", promedio));
            model.addAttribute("numeroEvaluaciones", totalEvals);

            return "dashboard/admin-instructor-detalle";
        }
        return "redirect:/panel/admin/instructores";
    }

    @PostMapping("/admin/instructores/{id}/actualizar")
    public String actualizarPerfilComercialInstructor(@PathVariable Long id,
            @RequestParam(required = false) java.math.BigDecimal salario,
            @RequestParam(required = false) String hojaDeVida,
            HttpSession session, RedirectAttributes redirectAttributes) {

        Optional<Instructor> instructorOpt = instructorRepository.findById(id);
        if (instructorOpt.isPresent()) {
            Instructor instructor = instructorOpt.get();
            if (salario != null)
                instructor.setSalario(salario);
            if (hojaDeVida != null)
                instructor.setHojaDeVida(hojaDeVida);

            instructorRepository.save(instructor);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Perfil financiero y hoja de vida actualizados correctamente.");
            return "redirect:/panel/admin/instructores/" + id;
        }
        return "redirect:/panel/admin/instructores";
    }

    // --- Panel de Administrador: Gestión de Estudiantes ---
    @GetMapping("/admin/estudiantes")
    public String listarEstudiantesAdmin(HttpSession session, Model model) {

        List<Estudiante> estudiantes = estudianteRepository.findAll();
        model.addAttribute("estudiantes", estudiantes);
        return "dashboard/admin-estudiantes";
    }

    @GetMapping("/admin/estudiantes/{id}")
    public String detalleEstudianteAdmin(@PathVariable Long id, HttpSession session, Model model) {

        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(id);
        if (estudianteOpt.isPresent()) {
            Estudiante estudiante = estudianteOpt.get();
            List<Pago> pagos = pagoRepository.findByEstudianteId(estudiante.getId());
            List<Clase> clases = claseRepository.findByEstudiantesId(estudiante.getId());
            List<Asistencia> asistencias = asistenciaRepository.findByEstudianteId(estudiante.getId());
            List<Calificacion> calificaciones = calificacionRepository.findByEstudianteId(estudiante.getId());

            model.addAttribute("estudiante", estudiante);
            model.addAttribute("pagos", pagos);
            model.addAttribute("clases", clases);
            model.addAttribute("asistencias", asistencias);
            model.addAttribute("calificaciones", calificaciones);
            return "dashboard/admin-estudiante-detalle";
        }
        return "redirect:/panel/admin/estudiantes";
    }

    @Transactional
    @PostMapping("/admin/estudiantes/{id}/suspender")
    public String suspenderEstudianteAdmin(@PathVariable Long id, HttpSession session,
            RedirectAttributes redirectAttributes) {

        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(id);
        if (estudianteOpt.isPresent()) {
            Estudiante estudiante = estudianteOpt.get();
            Usuario usuario = estudiante.getUsuario();
            if (usuario != null) {
                System.out.println("SUSPENDIENDO ACCESO PARA ESTUDIANTE: " + estudiante.getNombreCompleto());
                usuario.setActivo(false); // Dar de baja / Suspender acceso
                // Guardar la entidad dueña de la relación utilizando la cascada para reflejar
                // el estado en DB
                estudianteRepository.save(estudiante);

                redirectAttributes.addFlashAttribute("mensajeExito",
                        "El acceso del estudiante " + estudiante.getNombreCompleto()
                                + " ha sido inhabilitado exitosamente de la academia.");
            }
        }
        return "redirect:/panel/admin/estudiantes";
    }

    @PostMapping("/admin/instructores/aprobar/{id}")
    public String aprobarInstructor(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {

        Optional<Instructor> instructorOpt = instructorRepository.findById(id);
        if (instructorOpt.isPresent()) {
            Instructor instructor = instructorOpt.get();
            Usuario usuario = instructor.getUsuario();
            if (usuario != null) {
                usuario.setActivo(true); // Activa la cuenta para que pueda loguearse
                usuarioRepository.save(usuario);
                redirectAttributes.addFlashAttribute("mensajeExito",
                        "Instructor " + instructor.getNombre() + " ha sido aprobado y activado.");
            }
        }
        return "redirect:/panel/admin/instructores";
    }

    @Transactional
    @PostMapping("/admin/instructores/{id}/suspender")
    public String suspenderInstructorAdmin(@PathVariable Long id, HttpSession session,
            RedirectAttributes redirectAttributes) {

        Optional<Instructor> instructorOpt = instructorRepository.findById(id);
        if (instructorOpt.isPresent()) {
            Instructor instructor = instructorOpt.get();
            Usuario usuario = instructor.getUsuario();
            if (usuario != null) {
                System.out.println("SUSPENDIENDO ACCESO PARA INSTRUCTOR: " + instructor.getNombre());
                usuario.setActivo(false); // Dar de baja / Suspender acceso
                instructorRepository.save(instructor);

                redirectAttributes.addFlashAttribute("mensajeExito",
                        "El instructor " + instructor.getNombre() + " ha sido dado de baja e inhabilitado.");
            }
        }
        return "redirect:/panel/admin/instructores";
    }

    // --- Panel de Administrador: Gestión de Clases ---
    @GetMapping("/admin/clases")
    public String listarClasesAdmin(HttpSession session, Model model) {

        List<Clase> clases = claseRepository.findAll();
        List<Instructor> instructores = instructorRepository.findAll(); // Para el selector al crear clase

        model.addAttribute("clases", clases);
        model.addAttribute("instructores", instructores);
        model.addAttribute("nuevaClase", new Clase());
        return "dashboard/admin-clases";
    }

    @PostMapping("/admin/clases")
    public String crearClaseAdmin(Clase nuevaClase, HttpSession session, RedirectAttributes redirectAttributes) {

        // Si el instructor ID viene nulo / vacío desde el select, asegurarse de setear
        // null
        if (nuevaClase.getInstructor() != null && nuevaClase.getInstructor().getId() == null) {
            nuevaClase.setInstructor(null);
        }

        claseRepository.save(nuevaClase);
        redirectAttributes.addFlashAttribute("mensajeExito",
                "Clase '" + nuevaClase.getNombre() + "' creada exitosamente.");
        return "redirect:/panel/admin/clases";
    }

    // --- Detalle de una Clase (Para asignar alumnos) ---
    @GetMapping("/admin/clases/{id}")
    public String detalleClaseAdmin(@PathVariable Long id, HttpSession session, Model model) {

        Optional<Clase> claseOpt = claseRepository.findById(id);
        if (claseOpt.isPresent()) {
            Clase clase = claseOpt.get();
            List<Estudiante> todosEstudiantes = estudianteRepository.findAll();

            // Filtrar estudiantes que ya están en la clase para no mostrarlos en el select
            todosEstudiantes.removeAll(clase.getEstudiantes());
            List<Estudiante> noInscritos = new java.util.ArrayList<>(todosEstudiantes); // Assuming noInscritos is meant
                                                                                        // to be todosEstudiantes after
                                                                                        // filtering

            List<Asistencia> asistencias = asistenciaRepository.findByClaseId(clase.getId());
            List<Calificacion> calificaciones = calificacionRepository.findByClaseId(clase.getId());

            model.addAttribute("clase", clase);
            model.addAttribute("estudiantesDisponibles", todosEstudiantes);
            model.addAttribute("estudiantesNoInscritos", noInscritos);
            model.addAttribute("asistencias", asistencias);
            model.addAttribute("calificaciones", calificaciones);
            return "dashboard/admin-clase-detalle";
        }
        return "redirect:/panel/admin/clases";
    }

    @PostMapping("/admin/clases/{claseId}/agregar-alumno")
    public String agregarAlumnoAClase(@PathVariable Long claseId, @RequestParam Long estudianteId,
            HttpSession session, RedirectAttributes redirectAttributes) {

        Optional<Clase> claseOpt = claseRepository.findById(claseId);
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(estudianteId);

        if (claseOpt.isPresent() && estudianteOpt.isPresent()) {
            Clase clase = claseOpt.get();
            Estudiante estudiante = estudianteOpt.get();

            clase.getEstudiantes().add(estudiante);
            claseRepository.save(clase);

            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Estudiante '" + estudiante.getNombreCompleto() + "' agregado a la clase.");
        }

        return "redirect:/panel/admin/clases/" + claseId;
    }

    // --- Panel de Administrador: Gestión Financiera Total ---
    @GetMapping("/admin/pagos")
    public String listarPagosGlobalesAdmin(HttpSession session, Model model) {

        List<Pago> todosLosPagos = pagoRepository.findAll();
        // Sumatoria para el Ingreso Real (Recaudo)
        java.math.BigDecimal ingresosTotales = todosLosPagos.stream()
                .map(pago -> new java.math.BigDecimal("50000"))
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        // Lógica de Cartera (Deudores/Pendientes)
        List<Estudiante> todosLosEstudiantes = estudianteRepository.findAll();
        List<Estudiante> estudiantesEnMora = new java.util.ArrayList<>();

        for (Estudiante est : todosLosEstudiantes) {
            // Buscamos si el estudiante tiene pagos registrados
            List<Pago> pagosEstudiante = pagoRepository.findByEstudianteId(est.getId());
            if (pagosEstudiante.isEmpty()) {
                estudiantesEnMora.add(est);
            }
        }

        // Proyección de la deuda (Asumimos inscripción/mensualidad estándar de 50000
        // para el MVP)
        java.math.BigDecimal carteraTotal = new java.math.BigDecimal(estudiantesEnMora.size() * 50000);

        model.addAttribute("pagos", todosLosPagos);
        model.addAttribute("ingresosTotales", ingresosTotales);
        model.addAttribute("estudiantesEnMora", estudiantesEnMora);
        model.addAttribute("carteraTotal", carteraTotal);

        return "dashboard/admin-pagos";
    }

    // --- Panel de Administrador: Panel PQRS ---
    @GetMapping("/admin/pqrs")
    public String listarMensajesPQRS(HttpSession session, Model model) {

        List<com.example.demo.model.ContactoPQRS> mensajes = contactoPQRSRepository.findAllByOrderByFechaCreacionDesc();
        model.addAttribute("mensajes", mensajes);
        return "dashboard/admin-pqrs";
    }

    @PostMapping("/admin/pqrs/{id}/resolver")
    public String resolverPQRS(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {

        Optional<com.example.demo.model.ContactoPQRS> pqrsOpt = contactoPQRSRepository.findById(id);
        if (pqrsOpt.isPresent()) {
            com.example.demo.model.ContactoPQRS pqrs = pqrsOpt.get();
            pqrs.setEstado("Resuelto");
            contactoPQRSRepository.save(pqrs);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Mensaje de " + pqrs.getNombre() + " marcado como Resuelto.");
        }

        return "redirect:/panel/admin/pqrs";
    }
}
