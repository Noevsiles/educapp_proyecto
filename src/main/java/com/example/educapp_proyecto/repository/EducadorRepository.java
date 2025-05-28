package com.example.educapp_proyecto.repository;


import com.example.educapp_proyecto.model.Educador;
import com.example.educapp_proyecto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EducadorRepository extends JpaRepository<Educador, Long> {
    Optional<Educador> findByEmail(String emailEducador);
    Optional<Educador> findByusuario(Usuario usuario);

}
