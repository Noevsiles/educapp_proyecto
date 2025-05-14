package com.example.educapp_proyecto.service;


import com.example.educapp_proyecto.model.Sesion;

import java.util.List;

public interface SesionServiceInterface {

    List<Sesion> findAll();

    Sesion findById(Long id);

    Sesion save(Sesion sesion);

    void deleteById(Long id);


}
