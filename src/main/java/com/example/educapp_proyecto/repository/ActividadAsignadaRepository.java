package com.example.educapp_proyecto.repository;

import com.example.educapp_proyecto.model.ActividadAsignada;
import com.example.educapp_proyecto.model.Perro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActividadAsignadaRepository extends JpaRepository<ActividadAsignada, Long> {
    List<ActividadAsignada> findByPerro(Perro perro);
}