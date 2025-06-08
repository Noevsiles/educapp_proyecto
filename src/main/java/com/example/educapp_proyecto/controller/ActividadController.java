package com.example.educapp_proyecto.controller;

import com.example.educapp_proyecto.dto.ActividadDto;
import com.example.educapp_proyecto.model.Actividad;
import com.example.educapp_proyecto.repository.ActividadRepository;
import com.example.educapp_proyecto.service.impl.ActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Noelia Vázquez Siles
 * Controlador REST para gestionar las actividades de los planes de trabajo y las actividades generales.
 */
@RestController
@RequestMapping("/api/actividades")
public class ActividadController {
    @Autowired
    private ActividadService actividadService;

    @Autowired
    private ActividadRepository actividadRepository;

    /**
     * Crea una nueva actividad.
     *
     * @param actividad Objeto Actividad a crear.
     * @return La nueva actividad creada con estado HTTP 201.
     */
    // Crear una nueva actividad
    @PostMapping
    public ResponseEntity<Actividad> crearActividad(@RequestBody Actividad actividad) {
        Actividad nuevaActividad = actividadService.save(actividad);
        return new ResponseEntity<>(nuevaActividad, HttpStatus.CREATED);
    }

    /**
     * Obtiene todas las actividades registradas.
     *
     * @return Lista de actividades con estado HTTP 200.
     */
    // Obtener todas las actividades
    @GetMapping
    public ResponseEntity<List<Actividad>> obtenerTodasLasActividades() {
        List<Actividad> actividades = actividadService.findAll();
        return new ResponseEntity<>(actividades, HttpStatus.OK);
    }

    /**
     * Obtiene una actividad por su ID.
     *
     * @param id ID de la actividad.
     * @return La actividad correspondiente si existe, o HTTP 404 si no se encuentra.
     */
    // Obtener una actividad por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Actividad> obtenerActividadPorId(@PathVariable Long id) {
        try {
            Actividad actividad = actividadService.findById(id);
            return new ResponseEntity<>(actividad, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Actualiza el estado de completado de una actividad.
     *
     * @param id         ID de la actividad.
     * @param completado estado (true o false).
     * @return La actividad actualizada si se encuentra, o HTTP 404 si no existe.
     */
    // Actualizar el estado de la actividad (completado o no)
    @PutMapping("/{id}/estado")
    public ResponseEntity<Actividad> actualizarEstadoActividad(@PathVariable Long id, @RequestBody boolean completado) {
        try {
            Actividad actividadActualizada = actividadService.actualizarEstadoActividad(id, completado);
            return new ResponseEntity<>(actividadActualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Actualiza la duración de una actividad.
     *
     * @param id       ID de la actividad.
     * @param duracion duración en minutos.
     * @return La actividad actualizada si se encuentra, o HTTP 404 si no existe.
     */
    // Actualizar la duración de la actividad
    @PutMapping("/{id}/duracion")
    public ResponseEntity<Actividad> actualizarDuracionActividad(@PathVariable Long id, @RequestBody int duracion) {
        try {
            Actividad actividadActualizada = actividadService.actualizarDuracionActividad(id, duracion);
            return new ResponseEntity<>(actividadActualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina una actividad por su ID.
     *
     * @param id ID de la actividad a eliminar.
     * @return HTTP 204 si se elimina correctamente, o HTTP 404 si no se encuentra.
     */
    // Eliminar una actividad
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarActividad(@PathVariable Long id) {
        try {
            actividadService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Crea una actividad asociada a un plan de trabajo específico.
     *
     * @param idPlan ID del plan de trabajo.
     * @param dto    Datos de la actividad a crear.
     * @return HTTP 201 si se crea correctamente.
     */
    // Crear actividad para plan de trabajo
    @PostMapping("/plan/{idPlan}")
    public ResponseEntity<Void> crearActividadParaPlan(@PathVariable Long idPlan, @RequestBody ActividadDto dto) {
        actividadService.crearActividadParaPlan(idPlan, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Obtiene las actividades asociadas a un plan de trabajo.
     *
     * @param idPlan ID del plan de trabajo.
     * @return Lista de actividades en formato DTO.
     */
    // Obtener actividades por plan
    @GetMapping("/plan/{idPlan}")
    public ResponseEntity<List<ActividadDto>> obtenerPorPlan(@PathVariable Long idPlan) {
        List<Actividad> actividades = actividadRepository.findByPlanesTrabajo_Id(idPlan);
        List<ActividadDto> dtos = actividades.stream()
                .map(actividadService::convertirADto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Marca una actividad como completada.
     *
     * @param id ID de la actividad a marcar.
     * @return HTTP 200 si se marca correctamente, o HTTP 404 si no se encuentra.
     */
    // Marcar actividad como comletada
    @PutMapping("/{id}/completar")
    public ResponseEntity<Void> marcarComoCompletada(@PathVariable Long id) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));

        actividad.setCompletado(true);
        actividadRepository.save(actividad);

        return ResponseEntity.ok().build();
    }
}
