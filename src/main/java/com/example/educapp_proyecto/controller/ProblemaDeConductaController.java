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

/**
 * @author Noelia Vázquez Siles
 * Controlador REST para la gestión de problemas de conducta en perros.
 */
@RestController
@RequestMapping("/api/problemas-de-conducta")
public class ProblemaDeConductaController {

    @Autowired
    private ProblemaDeConductaService problemaDeConductaService;

    @Autowired
    private ProblemaDeConductaServiceInterface problemaService;

    /**
     * Crea un nuevo problema de conducta.
     *
     * @param problemaDeConducta Objeto {ProblemaDeConducta} con los datos del nuevo problema.
     * @return Problema creado con estado HTTP 201.
     */
    // Crear un problema de conducta
    @PostMapping
    public ResponseEntity<ProblemaDeConducta> crearProblemaDeConducta(@RequestBody ProblemaDeConducta problemaDeConducta) {
        ProblemaDeConducta nuevoProblemaDeConducta = problemaDeConductaService.save(problemaDeConducta);
        return new ResponseEntity<>(nuevoProblemaDeConducta, HttpStatus.CREATED);
    }

    /**
     * Obtiene todos los problemas de conducta, mostrando solo ID y nombre.
     *
     * @return Lista de problemas en formato {ProblemaConductaDto}.
     */
    // Obtener todos los problemas de conducta
    @GetMapping
    public ResponseEntity<List<ProblemaConductaDto>> obtenerTodosProblemasDeConducta() {
        List<ProblemaConductaDto> problemas = problemaDeConductaService.obtenerTodosSoloIdYNombre();
        return new ResponseEntity<>(problemas, HttpStatus.OK);
    }

    /**
     * Obtiene un problema de conducta por su ID.
     *
     * @param id ID del problema a buscar.
     * @return Problema encontrado o HTTP 404 si no existe.
     */
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

    /**
     * Actualiza un problema de conducta existente.
     *
     * @param id                 ID del problema a actualizar.
     * @param problemaDeConducta datos del problema.
     * @return Problema actualizado o HTTP 404 si no existe.
     */
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

    /**
     * Elimina un problema de conducta por su ID.
     *
     * @param id ID del problema a eliminar.
     * @return HTTP 204 si se elimina correctamente, o 404 si no se encuentra.
     */
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

    /**
     * Asigna una lista de problemas de conducta a un perro específico.
     *
     * @param dto DTO con el ID del perro y la lista de IDs de problemas a asignar.
     * @return HTTP 200 si la asignación se realiza correctamente.
     */
    // Asignar problemas de conducta a perros
    @PostMapping("/asignar")
    public ResponseEntity<Void> asignarProblemas(@RequestBody AsignacionProblemasDto dto) {
        problemaDeConductaService.asignarProblemasAPerro(dto.getIdPerro(), dto.getIdProblemas());
        return ResponseEntity.ok().build();
    }


    /**
     * Obtiene los problemas de conducta asignados a un perro, incluyendo las soluciones aplicables.
     *
     * @param idPerro ID del perro.
     * @return Lista de problemas con soluciones asociadas.
     */
    // Obtener problemas de un perro con soluciones
    @GetMapping("/por-perro/{idPerro}/con-soluciones")
    public ResponseEntity<List<ProblemaConductaDto>> getProblemasYSolucionesPorPerro(@PathVariable Long idPerro) {
        List<ProblemaConductaDto> resultado = problemaDeConductaService.obtenerProblemasYSolucionesDelPerro(idPerro);
        return ResponseEntity.ok(resultado);
    }
}
