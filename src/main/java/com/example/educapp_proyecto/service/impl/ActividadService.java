package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.Actividad;
import com.example.educapp_proyecto.repository.ActividadRepository;
import com.example.educapp_proyecto.service.ActividadServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActividadService implements ActividadServiceInterface {

    @Autowired
    private ActividadRepository actividadRepository;

    @Override
    public List<Actividad> findAll() {
        return actividadRepository.findAll();
    }

    @Override
    public Actividad findById(Long id) {
        Optional<Actividad> actividad = actividadRepository.findById(id);
        return actividad.orElseThrow(() -> new RuntimeException("Actividad no encontrada con el id: " + id));
    }

    @Override
    public Actividad save(Actividad actividad) {
        return actividadRepository.save(actividad);
    }

    @Override
    public void deleteById(Long id) {
        if (actividadRepository.existsById(id)) {
            actividadRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, actividad no encontrada con el id: " + id);
        }
    }

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
}
