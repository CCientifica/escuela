package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "escenario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Escenario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String lugar; // Patinódromo de la Alegría, Polideportivo Sur, etc.

    @Column(nullable = false)
    private String zona; // Pista de Velocidad (Anillo), Cancha de Hockey (Plana), etc.

    @Column(name = "capacidad_maxima", nullable = false)
    private Integer capacidadMaxima;
}
