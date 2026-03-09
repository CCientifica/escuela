package com.example.demo;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		System.out.println("Hello, World!");
	}

	@Bean
	CommandLineRunner initAdmin(UsuarioRepository usuarioRepository) {
		return args -> {
			String correoAdmin = "administrador@rollerspeed.com.co";
			Optional<Usuario> adminOpt = usuarioRepository.findByEmail(correoAdmin);
			if (adminOpt.isEmpty()) {
				Usuario admin = new Usuario();
				admin.setEmail(correoAdmin);
				admin.setPassword("123456"); // Idealmente cifrada, para propósitos del MVP usamos texto plano
				admin.setRol("ADMIN");
				admin.setActivo(true);
				usuarioRepository.save(admin);
				System.out.println("====== CUENTA ADMINISTRADOR CREADA ======");
				System.out.println("Usuario  : " + correoAdmin);
				System.out.println("Password : 123456");
				System.out.println("=========================================");
			}
		};
	}

}
