package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.model.ProblemaDeConducta;
import com.example.educapp_proyecto.service.impl.ProblemaDeConductaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problema-de-conducta")
public class ProblemaDeConductaController {
    @Autowired
    private ProblemaDeConductaService problemaDeConductaService;

    // Crear un problema de conducta
    @PostMapping
    public ResponseEntity<ProblemaDeConducta> crearProblemaDeConducta(@RequestBody ProblemaDeConducta problemaDeConducta) {
        ProblemaDeConducta nuevoProblemaDeConducta = problemaDeConductaService.save(problemaDeConducta);
        return new ResponseEntity<>(nuevoProblemaDeConducta, HttpStatus.CREATED);
    }

    // Obtener todos los problemas de conducta
    @GetMapping
    public ResponseEntity<List<ProblemaDeConducta>> obtenerTodosProblemasDeConducta() {
        List<ProblemaDeConducta> problemas = problemaDeConductaService.findAll();
        return new ResponseEntity<>(problemas, HttpStatus.OK);
    }

    // Obtener un problema de conducta por su ID
    @GetMapping("/{id}")
    public ResponseEntity<ProblemaDeConducta> obtenerProblemaDeConductaPorId(@PathVariable Long id) {
        try {
            ProblemaDeConducta problemaDeConducta = problemaDeConductaService.findById(id);
            return new ResponseEntity<>(problemaDeConducta, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar un problema de conducta
    @PutMapping("/{id}")
    public ResponseEntity<ProblemaDeConducta> actualizarProblemaDeConducta(@PathVariable Long id, @RequestBody ProblemaDeConducta problemaDeConducta) {
        try {
            ProblemaDeConducta problemaDeConductaActualizado = problemaDeConductaService.updateProblemaDeConducta(id, problemaDeConducta);
            return new ResponseEntity<>(problemaDeConductaActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un problema de conducta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProblemaDeConducta(@PathVariable Long id) {
        try {
            problemaDeConductaService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
