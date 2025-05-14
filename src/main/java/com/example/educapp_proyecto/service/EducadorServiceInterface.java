package com.example.educapp_proyecto.service;


import com.example.educapp_proyecto.model.Educador;

import java.util.List;

public interface EducadorServiceInterface {
    List<Educador> findAll();
    Educador findById(Long id);
    Educador save(Educador entity);
    void deleteById(Long id);
}
