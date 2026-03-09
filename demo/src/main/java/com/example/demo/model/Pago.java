package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @Column(name = "fecha_pago", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaPago;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private String concepto; // Mensualidad, Inscripción anual, Kit de uniforme

    @Column(nullable = false)
    private String estado; // Pendiente, Pagado, Vencido

    @Column(name = "metodo_pago")
    private String metodoPago; // Transferencia, Efectivo
}
