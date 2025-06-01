package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.Actividad;
import com.example.educapp_proyecto.model.SolucionAplicada;
import com.example.educapp_proyecto.repository.ActividadRepository;
import com.example.educapp_proyecto.repository.SolucionAplicadaRepository;
import com.example.educapp_proyecto.service.SolucionAplicadaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SolucionAplicadaService implements SolucionAplicadaServiceInterface {
    @Autowired
    private SolucionAplicadaRepository solucionAplicadaRepository;

    private ActividadRepository actividadRepository;

    // Encontrar todas las soluciones aplicadas
    @Override
    public List<SolucionAplicada> findAll() {
        return solucionAplicadaRepository.findAll();
    }

    // Encontrar solucion aplicada por email
    @Override
    public SolucionAplicada findById(Long id) {
        Optional<SolucionAplicada> solucionAplicada = solucionAplicadaRepository.findById(id);
        if (solucionAplicada.isPresent()) {
            return solucionAplicada.get();
        } else {
            throw new RuntimeException("Solución aplicada no encontrada con el id: " + id);
        }
    }

    // Guardar solucion aplicada
    @Override
    public SolucionAplicada save(SolucionAplicada solucionAplicada) {
        return solucionAplicadaRepository.save(solucionAplicada);
    }

    // Eliminar solucion aplicada por id
    @Override
    public void deleteById(Long id) {
        if (solucionAplicadaRepository.existsById(id)) {
            solucionAplicadaRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, solución aplicada no encontrada con el id: " + id);
        }
    }

    // Actualizar una solución aplicada
    public SolucionAplicada updateSolucionAplicada(Long id, SolucionAplicada solucionAplicada) {
        if (solucionAplicadaRepository.existsById(id)) {
            solucionAplicada.setIdSolucionAplicada(id);
            return solucionAplicadaRepository.save(solucionAplicada);
        } else {
            throw new RuntimeException("Solución aplicada no encontrada con el id: " + id);
        }
    }

    // Agregar una actividad a una solución aplicada
    @Override
    public Actividad agregarActividad(Long solucionAplicadaId, Actividad actividad) {
        SolucionAplicada solucionAplicada = solucionAplicadaRepository.findById(solucionAplicadaId)
                .orElseThrow(() -> new RuntimeException("Solución aplicada no encontrada con el id: " + solucionAplicadaId));

        actividad.setSolucionAplicada(solucionAplicada);
        return actividadRepository.save(actividad);
    }

    // Actualizar el progreso de una actividad
    @Override
    public void actualizarProgreso(Long actividadProgresoId, boolean completado) {
        Actividad actividadProgreso = actividadRepository.findById(actividadProgresoId)
                .orElseThrow(() -> new RuntimeException("Progreso de actividad no encontrado con el id: " + actividadProgresoId));
        actividadProgreso.setCompletado(completado);
        actividadRepository.save(actividadProgreso);
    }
}
