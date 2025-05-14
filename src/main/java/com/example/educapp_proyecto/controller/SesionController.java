package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.model.Sesion;
import com.example.educapp_proyecto.service.impl.SesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sesiones")
public class SesionController {

    @Autowired
    private SesionService sesionService;

    // Crear una sesión
    @PostMapping
    public ResponseEntity<Sesion> crearSesion(@RequestBody Sesion sesion) {
        Sesion nuevaSesion = sesionService.save(sesion);
        return new ResponseEntity<>(nuevaSesion, HttpStatus.CREATED);
    }

    // Obtener todas las sesiones
    @GetMapping
    public ResponseEntity<List<Sesion>> obtenerTodasLasSesiones() {
        List<Sesion> sesiones = sesionService.findAll();
        return new ResponseEntity<>(sesiones, HttpStatus.OK);
    }

    // Obtener una sesión por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Sesion> obtenerSesionPorId(@PathVariable Long id) {
        try {
            Sesion sesion = sesionService.findById(id);
            return new ResponseEntity<>(sesion, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar una sesión
    @PutMapping("/{id}")
    public ResponseEntity<Sesion> actualizarSesion(@PathVariable Long id, @RequestBody Sesion sesion) {
        try {
            Sesion sesionActualizada = sesionService.save(sesion);
            return new ResponseEntity<>(sesionActualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar una sesión
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSesion(@PathVariable Long id) {
        try {
            sesionService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
