package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
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
    @NotNull(message = "El estudiante es obligatorio")
    private Estudiante estudiante;

    @Column(name = "fecha_pago", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La fecha de pago es obligatoria")
    private LocalDate fechaPago;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El monto no puede ser negativo")
    private BigDecimal monto;

    @Column(nullable = false)
    @NotBlank(message = "El concepto es obligatorio")
    private String concepto; // Mensualidad, Inscripción anual, Kit de uniforme

    @Column(nullable = false)
    @NotBlank(message = "el estado es obligatorio")
    private String estado; // Pendiente, Pagado, Vencido

    @Column(name = "metodo_pago")
    private String metodoPago; // Transferencia, Efectivo
}
