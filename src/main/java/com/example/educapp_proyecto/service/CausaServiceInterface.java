package com.example.educapp_proyecto.service;


import com.example.educapp_proyecto.model.Causa;

import java.util.List;

public interface CausaServiceInterface {
    List<Causa> findAll();
    Causa findById(Long id);
    Causa save(Causa entity);
    void deleteById(Long id);
}
