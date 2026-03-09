package com.example.demo.repository;

import com.example.demo.model.AsignacionClase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AsignacionClaseRepository extends JpaRepository<AsignacionClase, Long> {

    // Custom query to detect temporal/spatial conflicts when creating a master
    // calendar item.
    Optional<AsignacionClase> findByDiaSemanaAndFranjaHorariaAndEscenarioId(String diaSemana, String franjaHoraria,
            Long escenarioId);
}
