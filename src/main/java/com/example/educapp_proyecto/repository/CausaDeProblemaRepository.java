package com.example.educapp_proyecto.repository;


import com.example.educapp_proyecto.model.CausaDeProblema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CausaDeProblemaRepository extends JpaRepository<CausaDeProblema, Long> {
}
