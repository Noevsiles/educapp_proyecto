package com.example.educapp_proyecto.repository;


import com.example.educapp_proyecto.model.Educador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EducadorRepository extends JpaRepository<Educador, Long> {
    Optional<Educador> findByEmail(String emailEducador);
}
