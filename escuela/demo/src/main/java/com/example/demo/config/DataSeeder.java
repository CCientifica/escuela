package com.example.demo.config;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class DataSeeder {

    @Value("${app.seeding.enabled:false}")
    private boolean seedingEnabled;

    @Value("${app.admin.password:admin_dev_password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner initData(
            UsuarioRepository usuarioRepository,
            EstudianteRepository estudianteRepository,
            InstructorRepository instructorRepository,
            NoticiaRepository noticiaRepository,
            TestimonioRepository testimonioRepository,
            EventoRepository eventoRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            if (!seedingEnabled) {
                return;
            }

            String testPassword = passwordEncoder.encode("123456");

            // =============================================
            // 1. USUARIOS DE PRUEBA
            // =============================================

            String emailAdmin = "adminprueba@test.com";
            if (usuarioRepository.findByEmail(emailAdmin).isEmpty()) {
                Usuario admin = new Usuario();
                admin.setEmail(emailAdmin);
                admin.setPassword(testPassword);
                admin.setRol("ADMIN");
                admin.setActivo(true);
                usuarioRepository.save(admin);
                System.out.println("✅ Usuario ADMIN creado: " + emailAdmin);
            }

            String emailInst = "instructorprueba@test.com";
            if (usuarioRepository.findByEmail(emailInst).isEmpty()) {
                Usuario userInst = new Usuario();
                userInst.setEmail(emailInst);
                userInst.setPassword(testPassword);
                userInst.setRol("INSTRUCTOR");
                userInst.setActivo(true);
                Instructor inst = new Instructor();
                inst.setNombre("Instructor Uno");
                inst.setCorreoElectronico(emailInst);
                inst.setEspecialidad("Velocidad");
                inst.setUsuario(userInst);
                instructorRepository.save(inst);
                System.out.println("✅ Usuario INSTRUCTOR creado: " + emailInst);
            }

            String emailEstv = "estudianteprueba@test.com";
            if (usuarioRepository.findByEmail(emailEstv).isEmpty()) {
                Usuario userEst = new Usuario();
                userEst.setEmail(emailEstv);
                userEst.setPassword(testPassword);
                userEst.setRol("ESTUDIANTE");
                userEst.setActivo(true);
                Estudiante est = new Estudiante();
                est.setNombreCompleto("Estudiante Uno");
                est.setDocumentoIdentidad("TEST123456");
                est.setCorreoElectronico(emailEstv);
                est.setNivel("Básico");
                est.setContactoEmergencia("123456789");
                est.setUsuario(userEst);
                estudianteRepository.save(est);
                System.out.println("✅ Usuario ESTUDIANTE creado: " + emailEstv);
            }

            String emailLegacyAdmin = "admin@rollerspeed.com";
            if (usuarioRepository.findByEmail(emailLegacyAdmin).isEmpty()) {
                Usuario admin = new Usuario();
                admin.setEmail(emailLegacyAdmin);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRol("ADMIN");
                admin.setActivo(true);
                usuarioRepository.save(admin);
                System.out.println("✅ Admin principal creado: " + emailLegacyAdmin);
            }

            // =============================================
            // 2. CONTENIDO DE PORTAL (ANTES ESTÁTICO EN HTML)
            // =============================================

            // Noticias
            if (noticiaRepository.count() == 0) {
                Noticia n1 = new Noticia();
                n1.setTitulo("¡Brillamos en el Patinódromo de la Alegría!");
                n1.setContenido("Nuestros patinadores de niveles Intermedio y Avanzado completaron con éxito el primer chequeo técnico del trimestre. Felicitamos a todos por mejorar sus tiempos de reacción y técnica de curva.");
                n1.setCategoria("Entrenamientos");
                n1.setFechaPublicacion(LocalDate.now());
                noticiaRepository.save(n1);

                Noticia n2 = new Noticia();
                n2.setTitulo("Rumbo al Festival Nacional de Escuelas");
                n2.setContenido("Nos preparamos para representar a Santa Marta en el próximo evento organizado por Fedepatín. Estamos ajustando los planes formativos para llegar en nuestra mejor versión.");
                n2.setCategoria("Competencias");
                n2.setFechaPublicacion(LocalDate.now());
                noticiaRepository.save(n2);

                System.out.println("✅ Noticias de portal cargadas.");
            }

            // Testimonios
            if (testimonioRepository.count() == 0) {
                testimonioRepository.save(new Testimonio(null, "María C.", "Madre de Familia",
                        "La disciplina y el amor con el que enseñan a los niños es increíble. Mi hija ha mejorado no solo en patinaje sino en su confianza personal.", 5));

                testimonioRepository.save(new Testimonio(null, "Andrés S.", "Alumno Nivel Avanzado",
                        "Entrenar con instructores certificados me ha permitido alcanzar el nivel competitivo que buscaba para mi categoría juvenil.", 5));

                testimonioRepository.save(new Testimonio(null, "Carlos P.", "Alumno Adulto Principiante",
                        "Un espacio súper seguro. Empecé de cero siendo adulto y ahora no falto a ninguna clase. ¡Gran equipo!", 4));

                System.out.println("✅ Testimonios de portal cargados.");
            }

            // Eventos
            if (eventoRepository.count() == 0) {
                eventoRepository.save(new Evento(null, "1° Festival de Escuelas Roller Speed",
                        LocalDate.of(2026, 4, 15), "Polideportivo Santa Marta",
                        "Jornada de integración para todos los niveles y entrega de insignias de progreso. (Evento Interno)."));

                eventoRepository.save(new Evento(null, "Santa Marta Night Run 2026",
                        LocalDate.of(2026, 3, 29), "Santa Marta",
                        "Acompañaremos en los puntos de hidratación apoyando la cultura deportiva de la ciudad."));

                eventoRepository.save(new Evento(null, "Parada Nacional Interclubes",
                        LocalDate.of(2026, 3, 20), "Buga, Valle",
                        "Seguimiento Nacional analizando técnicas para nuestras clases Élite."));

                System.out.println("✅ Eventos de portal cargados.");
            }
        };
    }
}
