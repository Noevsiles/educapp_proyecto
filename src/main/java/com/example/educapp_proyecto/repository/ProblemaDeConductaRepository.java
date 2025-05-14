package com.example.educapp_proyecto.repository;


import com.example.educapp_proyecto.model.ProblemaDeConducta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemaDeConductaRepository extends JpaRepository<ProblemaDeConducta, Long> {
}
