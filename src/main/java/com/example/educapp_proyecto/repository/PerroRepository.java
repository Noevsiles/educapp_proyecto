package com.example.educapp_proyecto.repository;


import com.example.educapp_proyecto.model.Perro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerroRepository extends JpaRepository<Perro, Long> {
}
