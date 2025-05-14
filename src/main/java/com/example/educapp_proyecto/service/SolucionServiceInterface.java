package com.example.educapp_proyecto.service;


import com.example.educapp_proyecto.model.Solucion;

import java.util.List;

public interface SolucionServiceInterface {

    List<Solucion> findAll();
    Solucion findById(Long id);
    Solucion save(Solucion entity);
    void deleteById(Long id);
}
