package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "instructores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String especialidad; // Velocidad o Artístico

    @Column(name = "anos_experiencia")
    private Integer anosExperiencia;

    private String telefono;

    @Column(nullable = false, unique = true)
    private String correoElectronico;

    @Column(name = "salario", precision = 10, scale = 2)
    private BigDecimal salario = BigDecimal.ZERO; // Salario pactado o costo por hora/mes

    @Column(columnDefinition = "TEXT")
    private String hojaDeVida; // Biografía, historial académico, diplomas, etc.

    // Relación con el sistema de acceso (RBAC)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;
}
