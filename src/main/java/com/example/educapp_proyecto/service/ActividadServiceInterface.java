package com.example.educapp_proyecto.service;



import com.example.educapp_proyecto.dto.ActividadDto;
import com.example.educapp_proyecto.model.Actividad;

import java.util.List;

public interface ActividadServiceInterface {
    List<Actividad> findAll();

    Actividad findById(Long id);

    Actividad save(Actividad actividad);

    void deleteById(Long id);

    // Actualizar el estado de una actividad (completado)
    Actividad actualizarEstadoActividad(Long id, boolean completado);

    // Actualizar la duración de la actividad
    Actividad actualizarDuracionActividad(Long id, int duracion);

    void crearActividadParaPlan(Long idPlan, ActividadDto dto);

    ActividadDto convertirADto(Actividad actividad);
}
