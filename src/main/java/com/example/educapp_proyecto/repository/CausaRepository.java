package com.example.educapp_proyecto.repository;


import com.example.educapp_proyecto.model.Causa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CausaRepository extends JpaRepository<Causa, Long> {
}
