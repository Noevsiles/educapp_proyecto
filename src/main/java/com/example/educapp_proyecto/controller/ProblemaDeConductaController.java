package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.dto.AsignacionProblemasDto;
import com.example.educapp_proyecto.dto.ProblemaConductaDto;
import com.example.educapp_proyecto.model.ProblemaDeConducta;
import com.example.educapp_proyecto.service.ProblemaDeConductaServiceInterface;
import com.example.educapp_proyecto.service.impl.ProblemaDeConductaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problemas-de-conducta")
public class ProblemaDeConductaController {
    @Autowired
    private ProblemaDeConductaService problemaDeConductaService;


    @Autowired
    private ProblemaDeConductaServiceInterface problemaService;

    // Crear un problema de conducta
    @PostMapping
    public ResponseEntity<ProblemaDeConducta> crearProblemaDeConducta(@RequestBody ProblemaDeConducta problemaDeConducta) {
        ProblemaDeConducta nuevoProblemaDeConducta = problemaDeConductaService.save(problemaDeConducta);
        return new ResponseEntity<>(nuevoProblemaDeConducta, HttpStatus.CREATED);
    }

    // Obtener todos los problemas de conducta
    @GetMapping
    public ResponseEntity<List<ProblemaConductaDto>> obtenerTodosProblemasDeConducta() {
        List<ProblemaConductaDto> problemas = problemaDeConductaService.obtenerTodosSoloIdYNombre();
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

    // Asignar problemas de conducta a perros
    @PostMapping("/asignar")
    public ResponseEntity<Void> asignarProblemas(@RequestBody AsignacionProblemasDto dto) {
        problemaDeConductaService.asignarProblemasAPerro(dto.getIdPerro(), dto.getIdProblemas());
        return ResponseEntity.ok().build();
    }


    // Obtener problemas de un perro con soluciones
    @GetMapping("/por-perro/{idPerro}/con-soluciones")
    public ResponseEntity<List<ProblemaConductaDto>> getProblemasYSolucionesPorPerro(@PathVariable Long idPerro) {
        List<ProblemaConductaDto> resultado = problemaDeConductaService.obtenerProblemasYSolucionesDelPerro(idPerro);
        return ResponseEntity.ok(resultado);
    }



}
