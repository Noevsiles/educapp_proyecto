package com.example.educapp_proyecto.service;


import com.example.educapp_proyecto.model.Tarifa;

import java.util.List;

public interface TarifaServiceInterface {

    List<Tarifa> findAll();

    Tarifa findById(Long id);

    Tarifa save(Tarifa tarifa);

    void deleteById(Long id);
}
