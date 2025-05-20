package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.model.Educador;
import com.example.educapp_proyecto.service.impl.EducadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/educador")
public class EducadorController {
    @Autowired
    private EducadorService educadorService;

    // Crear un educador
    @PostMapping
    public ResponseEntity<Educador> crearEducador(@RequestBody Educador educador) {
        Educador nuevoEducador = educadorService.save(educador);
        return new ResponseEntity<>(nuevoEducador, HttpStatus.CREATED);
    }

    // Obtener todos los educadores
    @GetMapping
    public ResponseEntity<List<Educador>> obtenerTodosEducadores() {
        List<Educador> educadores = educadorService.findAll();
        return new ResponseEntity<>(educadores, HttpStatus.OK);
    }

    // Obtener un educador por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Educador> obtenerEducadorPorId(@PathVariable Long id) {
        try {
            Educador educador = educadorService.findById(id);
            return new ResponseEntity<>(educador, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar un educador
    @PutMapping("/{id}")
    public ResponseEntity<Educador> actualizarEducador(@PathVariable Long id, @RequestBody Educador educador) {
        try {
            Educador educadorActualizado = educadorService.updateEducador(id, educador);
            return new ResponseEntity<>(educadorActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un educador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEducador(@PathVariable Long id) {
        try {
            educadorService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
