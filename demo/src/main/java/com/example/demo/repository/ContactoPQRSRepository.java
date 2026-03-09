package com.example.demo.repository;

import com.example.demo.model.ContactoPQRS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactoPQRSRepository extends JpaRepository<ContactoPQRS, Long> {
    List<ContactoPQRS> findByEstado(String estado);

    List<ContactoPQRS> findAllByOrderByFechaCreacionDesc();
}
