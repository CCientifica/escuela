package com.example.demo.model;

import jakarta.persistence.*;
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
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String rol; // ADMIN, ESTUDIANTE, INSTRUCTOR

    @Column(nullable = false)
    private Boolean activo = true; // Para suspender o habilitar cuentas

    // OneToOne relationships mapped by the Estudiante and Instructor entities
    // implicitly via referencing Usuario
}
