package com.example.educapp_proyecto.repository;


import com.example.educapp_proyecto.model.Cliente;
import com.example.educapp_proyecto.model.Educador;
import com.example.educapp_proyecto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByEducador(Educador educador);
    Optional<Cliente> findByUsuario(Usuario usuario);
    Optional<Cliente> findByEmail(String email);


}
