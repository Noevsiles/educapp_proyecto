package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.model.Solucion;
import com.example.educapp_proyecto.service.impl.SolucionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/soluciones")
public class SolucionController {
    @Autowired
    private SolucionService solucionService;

    // Crear una solución
    @PostMapping
    public ResponseEntity<Solucion> crearSolucion(@RequestBody Solucion solucion) {
        Solucion nuevaSolucion = solucionService.save(solucion);
        return new ResponseEntity<>(nuevaSolucion, HttpStatus.CREATED);
    }

    // Obtener todas las soluciones
    @GetMapping
    public ResponseEntity<List<Solucion>> obtenerTodasLasSoluciones() {
        List<Solucion> soluciones = solucionService.findAll();
        return new ResponseEntity<>(soluciones, HttpStatus.OK);
    }

    // Obtener una solución por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Solucion> obtenerSolucionPorId(@PathVariable Long id) {
        try {
            Solucion solucion = solucionService.findById(id);
            return new ResponseEntity<>(solucion, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar una solución
    @PutMapping("/{id}")
    public ResponseEntity<Solucion> actualizarSolucion(@PathVariable Long id, @RequestBody Solucion solucion) {
        try {
            Solucion solucionActualizada = solucionService.updateSolucion(id, solucion);
            return new ResponseEntity<>(solucionActualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar una solución
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolucion(@PathVariable Long id) {
        try {
            solucionService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
