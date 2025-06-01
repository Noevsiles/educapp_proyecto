package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.dto.ActividadDto;
import com.example.educapp_proyecto.model.Actividad;
import com.example.educapp_proyecto.model.PlanTrabajo;
import com.example.educapp_proyecto.repository.ActividadRepository;
import com.example.educapp_proyecto.repository.PlanTrabajoRepository;
import com.example.educapp_proyecto.service.ActividadServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActividadService implements ActividadServiceInterface {

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private PlanTrabajoRepository planTrabajoRepository;

    @Override
    public List<Actividad> findAll() {
        return actividadRepository.findAll();
    }

    @Override
    public Actividad findById(Long id) {
        Optional<Actividad> actividad = actividadRepository.findById(id);
        return actividad.orElseThrow(() -> new RuntimeException("Actividad no encontrada con el id: " + id));
    }

    // Guarar una actividad
    @Override
    public Actividad save(Actividad actividad) {
        return actividadRepository.save(actividad);
    }

    // Eliminar una actividad por su id
    @Override
    public void deleteById(Long id) {
        if (actividadRepository.existsById(id)) {
            actividadRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, actividad no encontrada con el id: " + id);
        }
    }

    // Actualizar el estado de una activiadad (completada o no)
    @Override
    public Actividad actualizarEstadoActividad(Long id, boolean completado) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada con el id: " + id));

        actividad.setCompletado(completado);
        return actividadRepository.save(actividad);
    }

    // Actualizar la duración de la actividad
    @Override
    public Actividad actualizarDuracionActividad(Long id, int duracion) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada con el id: " + id));

        actividad.setDuracion(duracion);
        return actividadRepository.save(actividad);
    }

    // Crear actividad para asignar a plan de trabajo
    @Override
    public void crearActividadParaPlan(Long idPlan, ActividadDto dto) {
        PlanTrabajo plan = planTrabajoRepository.findById(idPlan)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));

        Actividad actividad = new Actividad();
        actividad.setNombre(dto.getNombre());
        actividad.setDescripcion(dto.getDescripcion());
        actividad.setDuracion(dto.getDuracion());
        actividad.setCompletado(false); // Por defecto
        actividad.getPlanesTrabajo().add(plan);

        actividadRepository.save(actividad);
    }

    // Convertir acticidad a dto
    @Override
    public ActividadDto convertirADto(Actividad actividad) {
        ActividadDto dto = new ActividadDto();
        dto.setIdActividad(actividad.getIdActividad());
        dto.setNombre(actividad.getNombre());
        dto.setDescripcion(actividad.getDescripcion());
        dto.setDuracion(actividad.getDuracion());
        dto.setCompletado(actividad.isCompletado());
        return dto;
    }
}
