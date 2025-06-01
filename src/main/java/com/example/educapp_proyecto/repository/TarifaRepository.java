package com.example.educapp_proyecto.repository;


import com.example.educapp_proyecto.model.Educador;
import com.example.educapp_proyecto.model.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {

    List<Tarifa> findByEducador(Educador educador);

    List<Tarifa> findByEducador_IdEducador(Long idEducador);

}
