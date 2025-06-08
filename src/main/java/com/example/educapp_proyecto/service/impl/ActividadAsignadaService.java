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

/**
 * Servicio que gestiona la asignación de actividades a los perros.
 * Permite asignar nuevas actividades, consultar las actividades de un perro,
 * y marcar actividades como completadas.
 *
 * @author Noelia Vázquez Siles
 */
@Service
public class ActividadAsignadaService implements ActividadAsignadaServiceInterface {

    @Autowired
    private ActividadAsignadaRepository asignadaRepo;

    @Autowired
    private ActividadRepository actividadRepo;

    @Autowired
    private PerroRepository perroRepo;


    // Asignar actividades al perro
    /**
     * Asigna una actividad específica a un perro.
     *
     * @param actividadId ID de la actividad a asignar.
     * @param perroId     ID del perro al que se le asignará la actividad.
     * @throws RuntimeException si no se encuentra la actividad o el perro.
     */
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
    /**
     * Obtiene la lista de actividades asignadas a un perro por su ID.
     *
     * @param perroId ID del perro.
     * @return Lista de actividades asignadas en forma de DTOs.
     * @throws RuntimeException si el perro no existe.
     */
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

    /**
     * Marca una actividad asignada como completada.
     *
     * @param idAsignacion ID de la asignación de actividad.
     * @throws RuntimeException si no se encuentra la asignación.
     */
    // Marcar una actividad como completada
    @Override
    public void marcarComoCompletada(Long idAsignacion) {
        ActividadAsignada asignada = asignadaRepo.findById(idAsignacion).orElseThrow();
        asignada.setCompletada(true);
        asignadaRepo.save(asignada);
    }
}
