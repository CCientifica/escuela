package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "asignacion_clase")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionClase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dia_semana", nullable = false)
    private String diaSemana; // Lunes, Martes, etc.

    @Column(name = "franja_horaria", nullable = false)
    private String franjaHoraria; // 4:00 PM - 6:00 PM

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id", nullable = false)
    private PlanEntrenamiento plan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instructor_id", nullable = false)
    private Instructor instructor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "escenario_id", nullable = false)
    private Escenario escenario;
}
