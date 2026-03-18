package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Column(nullable = false)
    @NotBlank(message = "El rol es obligatorio")
    private String rol; // ADMIN, ESTUDIANTE, INSTRUCTOR

    @Column(nullable = false)
    private Boolean activo = true; // Para suspender o habilitar cuentas

    // OneToOne relationships mapped by the Estudiante and Instructor entities
    // implicitly via referencing Usuario
}
