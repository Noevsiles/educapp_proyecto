package com.example.educapp_proyecto.service;


import com.example.educapp_proyecto.model.Actividad;
import com.example.educapp_proyecto.model.SolucionAplicada;

import java.util.List;

public interface SolucionAplicadaServiceInterface {
    List<SolucionAplicada> findAll();
    SolucionAplicada findById(Long id);
    SolucionAplicada save(SolucionAplicada entity);
    void deleteById(Long id);

    // Métodos para trabajar con actividades y su progreso
    Actividad agregarActividad(Long solucionAplicadaId, Actividad actividad);

    void actualizarProgreso(Long actividadProgresoId, boolean completado);
}
