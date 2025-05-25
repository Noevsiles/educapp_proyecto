package com.example.educapp_proyecto.repository;


import com.example.educapp_proyecto.model.Cliente;
import com.example.educapp_proyecto.model.Educador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByEducador(Educador educador);

}
