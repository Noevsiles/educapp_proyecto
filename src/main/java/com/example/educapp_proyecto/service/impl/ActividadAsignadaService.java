package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.dto.ActividadAsignadaResponseDto;
import com.example.educapp_proyecto.model.Actividad;
import com.example.educapp_proyecto.model.ActividadAsignada;
import com.example.educapp_proyecto.model.Perro;
import com.example.educapp_proyecto.repository.ActividadAsignadaRepository;
import com.example.educapp_proyecto.repository.ActividadRepository;
import com.example.educapp_proyecto.repository.PerroRepository;
import com.example.educapp_proyecto.service.ActividadAsignadaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActividadAsignadaService implements ActividadAsignadaServiceInterface {

    @Autowired
    private ActividadAsignadaRepository asignadaRepo;

    @Autowired
    private ActividadRepository actividadRepo;

    @Autowired
    private PerroRepository perroRepo;


    // Asignar actividades al perro
    @Override
    public void asignarActividad(Long actividadId, Long perroId) {
        Actividad actividad = actividadRepo.findById(actividadId)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada: " + actividadId));

        Perro perro = perroRepo.findById(perroId)
                .orElseThrow(() -> new RuntimeException("Perro no encontrado: " + perroId));

        ActividadAsignada asignada = new ActividadAsignada();
        asignada.setActividad(actividad);
        asignada.setPerro(perro);
        asignada.setCompletada(false);

        asignadaRepo.save(asignada);
    }

    // Obtener las actividades del perro por su id
    @Override
    public List<ActividadAsignadaResponseDto> obtenerActividadesPorPerro(Long perroId) {
        Perro perro = perroRepo.findById(perroId).orElseThrow();
        return asignadaRepo.findByPerro(perro).stream().map(asignada -> {
            ActividadAsignadaResponseDto dto = new ActividadAsignadaResponseDto();
            dto.setId(asignada.getId());
            dto.setNombreActividad(asignada.getActividad().getNombre());
            dto.setDescripcion(asignada.getActividad().getDescripcion());
            dto.setCompletada(asignada.isCompletada());
            return dto;
        }).toList();
    }

    // Marcar una actividad como completada
    @Override
    public void marcarComoCompletada(Long idAsignacion) {
        ActividadAsignada asignada = asignadaRepo.findById(idAsignacion).orElseThrow();
        asignada.setCompletada(true);
        asignadaRepo.save(asignada);
    }
}
