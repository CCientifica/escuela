package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.ValoracionInstructor;
import java.util.List;

@Repository
public interface ValoracionInstructorRepository extends JpaRepository<ValoracionInstructor, Long> {
    List<ValoracionInstructor> findByInstructorId(Long instructorId);

    List<ValoracionInstructor> findByEstudianteId(Long estudianteId);
}
