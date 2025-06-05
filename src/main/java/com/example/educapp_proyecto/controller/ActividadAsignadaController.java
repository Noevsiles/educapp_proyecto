package com.example.educapp_proyecto.controller;

import com.example.educapp_proyecto.dto.ActividadAsignadaRequestDto;
import com.example.educapp_proyecto.dto.ActividadAsignadaResponseDto;
import com.example.educapp_proyecto.service.ActividadAsignadaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Noelia Vázquez Siles
 * Controlador REST para gestionar la asignación de actividades a perros.
 */
@RestController
@RequestMapping("/api/actividades-asignadas")
public class ActividadAsignadaController {

    @Autowired
    private ActividadAsignadaServiceInterface service;

    /**
     * Asigna una actividad a un perro.
     *
     * @param dto Objeto DTO que contiene los IDs de la actividad y del perro.
     * @return Respuesta HTTP con estado 201 (CREATED) si la operación fue exitosa.
     */
    @PostMapping
    public ResponseEntity<Void> asignarActividad(@RequestBody ActividadAsignadaRequestDto dto) {
        service.asignarActividad(dto.getActividadId(), dto.getPerroId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Obtiene la lista de actividades asignadas a un perro específico.
     *
     * @param perroId ID del perro.
     * @return Lista de objetos DTO con la información de las actividades asignadas.
     */
    @GetMapping("/perro/{perroId}")
    public List<ActividadAsignadaResponseDto> getActividadesPorPerro(@PathVariable Long perroId) {
        return service.obtenerActividadesPorPerro(perroId);
    }

    /**
     * Marca una actividad asignada como completada.
     *
     * @param id ID de la actividad asignada.
     * @return Respuesta HTTP con estado 200 (OK) si la operación fue exitosa.
     */
    @PatchMapping("/{id}/completar")
    public ResponseEntity<Void> marcarComoCompletada(@PathVariable Long id) {
        service.marcarComoCompletada(id);
        return ResponseEntity.ok().build();
    }
}
