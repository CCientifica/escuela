package com.example.demo.config;

import com.example.demo.model.Estudiante;
import com.example.demo.model.Instructor;
import com.example.demo.model.Usuario;
import com.example.demo.repository.EstudianteRepository;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class DataSeeder {

    @Value("${app.seeding.enabled:false}")
    private boolean seedingEnabled;

    @Value("${app.admin.password:admin_dev_password}")
    private String adminPassword;

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, 
                                    EstudianteRepository estudianteRepository,
                                    InstructorRepository instructorRepository,
                                    PasswordEncoder passwordEncoder) {
        return args -> {
            if (!seedingEnabled) {
                return;
            }

            String testPassword = passwordEncoder.encode("123456");

            // 1. ADMIN PRUEBA
            String emailAdmin = "adminprueba@test.com";
            if (usuarioRepository.findByEmail(emailAdmin).isEmpty()) {
                Usuario admin = new Usuario();
                admin.setEmail(emailAdmin);
                admin.setPassword(testPassword);
                admin.setRol("ADMIN");
                admin.setActivo(true);
                usuarioRepository.save(admin);
                System.out.println("✅ Usuario ADMIN de prueba creado: " + emailAdmin);
            }

            // 2. INSTRUCTOR PRUEBA
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
                System.out.println("✅ Usuario INSTRUCTOR de prueba creado: " + emailInst);
            }

            // 3. ESTUDIANTE PRUEBA
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
                System.out.println("✅ Usuario ESTUDIANTE de prueba creado: " + emailEstv);
            }

            // Centralized Legacy Admin (If needed)
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
        };
    }
}
