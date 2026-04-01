package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "testimonios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Testimonio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String autor;

    @Column(name = "rol_autor")
    private String rolAutor; // Ej. "Madre de Familia"

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenido;

    private Integer estrellas; // 1 a 5
}
