package com.example.educapp_proyecto.controller;

import com.example.educapp_proyecto.model.Actividad;
import com.example.educapp_proyecto.service.impl.ActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividades")
public class ActividadController {
    @Autowired
    private ActividadService actividadService;

    // Crear una nueva actividad
    @PostMapping
    public ResponseEntity<Actividad> crearActividad(@RequestBody Actividad actividad) {
        Actividad nuevaActividad = actividadService.save(actividad);
        return new ResponseEntity<>(nuevaActividad, HttpStatus.CREATED);
    }

    // Obtener todas las actividades
    @GetMapping
    public ResponseEntity<List<Actividad>> obtenerTodasLasActividades() {
        List<Actividad> actividades = actividadService.findAll();
        return new ResponseEntity<>(actividades, HttpStatus.OK);
    }

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
}
