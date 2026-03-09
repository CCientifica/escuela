package com.example.demo.repository;

import com.example.demo.model.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {
    List<Clase> findByInstructorId(Long instructorId);

    List<Clase> findByEstudiantesId(Long estudianteId);
}
