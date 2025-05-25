package com.example.educapp_proyecto.service;


import com.example.educapp_proyecto.model.Educador;

import java.util.List;
import java.util.Optional;

public interface EducadorServiceInterface {
    List<Educador> findAll();
    Educador findById(Long id);
    Optional<Educador> findByEmail(String email);
    Educador save(Educador entity);
    void deleteById(Long id);
}
