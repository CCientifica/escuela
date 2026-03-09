package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "plan_entrenamiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanEntrenamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre; // Semilleros (Iniciación), Formativo (Intermedio), Élite (Competencia)

    @Column(nullable = false, length = 500)
    private String enfoque;

    @Column(nullable = false)
    private String frecuencia;
}
