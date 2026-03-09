package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "estudiantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(name = "documento_identidad", nullable = false, unique = true)
    private String documentoIdentidad;

    @Column(name = "correo_electronico", unique = true)
    private String correoElectronico;

    @Column(name = "fecha_nacimiento")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @Column
    private String genero; // Masculino, Femenino, Otro

    @Column
    private String telefono;

    @Column(nullable = false)
    private String nivel; // Básico, Intermedio, Avanzado

    private String eps;

    @Column(name = "contacto_emergencia", nullable = false)
    private String contactoEmergencia;

    @Column(name = "estado_pago", nullable = false)
    private String estadoPago = "A Paz y Salvo"; // Default value: "A Paz y Salvo" or "Pendiente"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private Instructor instructorAsignado; // Para uso futuro (Evaluación por el docente)

    // Relación con el sistema de acceso (RBAC)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;
}
