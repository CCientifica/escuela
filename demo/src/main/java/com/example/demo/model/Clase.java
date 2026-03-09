package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "clases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre; // Ej. "Semilleros Grupo 1"

    @Column(nullable = false)
    private String nivel; // Básico, Intermedio, Élite

    @Column(nullable = false)
    private String horario; // Ej. Lunes y Miércoles 4:00 PM - 5:30 PM

    @Column(nullable = false)
    private String escenario; // Ej. Patinódromo de la Alegría

    // Relación Muchos a Uno con Instructor
    // Una clase tiene 1 instructor asignado. Un instructor puede tener N clases.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    // Relación Muchos a Muchos con Estudiante
    // Varios estudiantes en una clase, y un estudiante puede estar en varias clases
    // si es necesario.
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "clase_estudiantes", joinColumns = @JoinColumn(name = "clase_id"), inverseJoinColumns = @JoinColumn(name = "estudiante_id"))
    private Set<Estudiante> estudiantes = new HashSet<>();
}
