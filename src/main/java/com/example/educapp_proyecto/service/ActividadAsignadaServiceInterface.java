package com.example.educapp_proyecto.service;

import com.example.educapp_proyecto.dto.ActividadAsignadaResponseDto;
import java.util.List;

public interface ActividadAsignadaServiceInterface {
    void asignarActividad(Long actividadId, Long perroId);
    List<ActividadAsignadaResponseDto> obtenerActividadesPorPerro(Long perroId);
    void marcarComoCompletada(Long idAsignacion);
}