package com.example.educapp_proyecto.repository;

import com.example.educapp_proyecto.model.SolucionAplicada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolucionAplicadaRepository extends JpaRepository<SolucionAplicada, Long> {
}
