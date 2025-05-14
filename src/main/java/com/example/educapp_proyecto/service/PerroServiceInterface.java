package com.example.educapp_proyecto.service;


import com.example.educapp_proyecto.model.Perro;

import java.util.List;

public interface PerroServiceInterface {
    List<Perro> findAll();
    Perro findById(Long id);
    Perro save(Perro entity);
    void deleteById(Long id);
}
