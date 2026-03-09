package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("Validando y limpiando esquema de base de datos de columnas antiguas...");
            // Intentar eliminar las columnas 'email' y 'password' si existen en estudiantes
            try {
                jdbcTemplate.execute("ALTER TABLE estudiantes DROP COLUMN IF EXISTS email;");
                jdbcTemplate.execute("ALTER TABLE estudiantes DROP COLUMN IF EXISTS password;");
                jdbcTemplate.execute("ALTER TABLE estudiantes DROP COLUMN IF EXISTS nombre;");
                jdbcTemplate.execute("ALTER TABLE estudiantes DROP COLUMN IF EXISTS apellidos;");
                System.out.println("Columnas legacy limpiadas de tabla estudiantes.");
            } catch (Exception e) {
                System.out.println("Skip: Columnas en estudiantes ya no existen o no se pueden alterar.");
            }

            // Intentar eliminar lo mismo en instructores si existiera
            try {
                jdbcTemplate.execute("ALTER TABLE instructor DROP COLUMN IF EXISTS password;");
                jdbcTemplate.execute("ALTER TABLE instructor DROP COLUMN IF EXISTS clave;");
                jdbcTemplate.execute("ALTER TABLE instructor DROP COLUMN IF EXISTS nombre;");
                jdbcTemplate.execute("ALTER TABLE instructor DROP COLUMN IF EXISTS apellidos;");
            } catch (Exception e) {
            }

            // Ya no eliminamos correo_electronico porque ahora es parte oficial del modelo.
        } catch (Exception e) {
            System.out.println("Error ejecutando limpieza de DB: " + e.getMessage());
        }
    }
}
