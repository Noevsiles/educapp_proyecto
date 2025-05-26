package com.example.educapp_proyecto.repository;


import com.example.educapp_proyecto.model.Perro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerroRepository extends JpaRepository<Perro, Long> {
    List<Perro> findByCliente_Educador_Email(String emailEducador);

}
