package com.example.educapp_proyecto.service;


import com.example.educapp_proyecto.model.ProblemaDeConducta;

import java.util.List;

public interface ProblemaDeConductaServiceInterface {
    List<ProblemaDeConducta> findAll();
    ProblemaDeConducta findById(Long id);
    ProblemaDeConducta save(ProblemaDeConducta entity);
    void deleteById(Long id);
}
