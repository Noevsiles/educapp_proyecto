package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.model.Actividad;
import com.example.educapp_proyecto.model.SolucionAplicada;
import com.example.educapp_proyecto.service.impl.SolucionAplicadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/soluciones-aplicadas")
public class SolucionAplicadaController {
    @Autowired
    private SolucionAplicadaService solucionAplicadaService;

    // Crear una solución aplicada
    @PostMapping
    public ResponseEntity<SolucionAplicada> crearSolucionAplicada(@RequestBody SolucionAplicada solucionAplicada) {
        SolucionAplicada nuevaSolucionAplicada = solucionAplicadaService.save(solucionAplicada);
        return new ResponseEntity<>(nuevaSolucionAplicada, HttpStatus.CREATED);
    }

    // Obtener todas las soluciones aplicadas
    @GetMapping
    public ResponseEntity<List<SolucionAplicada>> obtenerTodasSolucionesAplicadas() {
        List<SolucionAplicada> soluciones = solucionAplicadaService.findAll();
        return new ResponseEntity<>(soluciones, HttpStatus.OK);
    }

    // Obtener una solución aplicada por su ID
    @GetMapping("/{id}")
    public ResponseEntity<SolucionAplicada> obtenerSolucionAplicadaPorId(@PathVariable Long id) {
        try {
            SolucionAplicada solucionAplicada = solucionAplicadaService.findById(id);
            return new ResponseEntity<>(solucionAplicada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar una solución aplicada
    @PutMapping("/{id}")
    public ResponseEntity<SolucionAplicada> actualizarSolucionAplicada(@PathVariable Long id, @RequestBody SolucionAplicada solucionAplicada) {
        try {
            SolucionAplicada solucionAplicadaActualizada = solucionAplicadaService.updateSolucionAplicada(id, solucionAplicada);
            return new ResponseEntity<>(solucionAplicadaActualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Agregar una actividad a una solución aplicada
    @PostMapping("/{id}/actividad")
    public ResponseEntity<Actividad> agregarActividad(@PathVariable Long id, @RequestBody Actividad actividad) {
        try {
            Actividad nuevaActividad = solucionAplicadaService.agregarActividad(id, actividad);
            return new ResponseEntity<>(nuevaActividad, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar el progreso de una actividad
    @PutMapping("/actividad/{id}")
    public ResponseEntity<Void> actualizarProgreso(@PathVariable Long id, @RequestBody boolean completado) {
        try {
            solucionAplicadaService.actualizarProgreso(id, completado);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar una solución aplicada
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolucionAplicada(@PathVariable Long id) {
        try {
            solucionAplicadaService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
