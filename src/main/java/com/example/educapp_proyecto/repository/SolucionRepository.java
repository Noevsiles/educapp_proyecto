package com.example.educapp_proyecto.repository;

import com.example.educapp_proyecto.model.Solucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolucionRepository extends JpaRepository<Solucion, Long> {
}
