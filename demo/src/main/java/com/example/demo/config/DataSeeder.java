package com.example.demo.config;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initData(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Verificar si ya existe el administrador
            if (usuarioRepository.findByEmail("admin@rollerspeed.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setEmail("admin@rollerspeed.com");
                admin.setPassword(passwordEncoder.encode("admin123")); // Contraseña por defecto
                admin.setRol("ADMIN");
                admin.setActivo(true);

                usuarioRepository.save(admin);
                System.out.println(
                        "Usuario Administrador creado por defecto. Email: admin@rollerspeed.com / Password: admin123");
            }
        };
    }
}
