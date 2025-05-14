package com.example.educapp_proyecto.service;

import com.example.educapp_proyecto.model.CausaDeProblema;

import java.util.List;

public interface CausaDeProblemaServiceInterface {
    List<CausaDeProblema> findAll();
    CausaDeProblema findById(Long id);
    CausaDeProblema save(CausaDeProblema entity);
    void deleteById(Long id);
}
