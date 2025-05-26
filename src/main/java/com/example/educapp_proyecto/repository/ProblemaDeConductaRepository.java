package com.example.educapp_proyecto.repository;


import com.example.educapp_proyecto.model.ProblemaDeConducta;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProblemaDeConductaRepository extends JpaRepository<ProblemaDeConducta, Long> {

    List<ProblemaDeConducta> findByPerro_IdPerro(Long perroIdPerro);
}
