package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.model.Actividad;
import com.example.educapp_proyecto.model.SolucionAplicada;
import com.example.educapp_proyecto.service.impl.SolucionAplicadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** @author Noelia Vázquez Siles
 * Controlador REST para la gestión de soluciones aplicadas a problemas de conducta.
 */
@RestController
@RequestMapping("/api/soluciones-aplicadas")
public class SolucionAplicadaController {
    @Autowired
    private SolucionAplicadaService solucionAplicadaService;

    /**
     * Crea una nueva solución aplicada.
     *
     * @param solucionAplicada Objeto SolucionAplicada a crear.
     * @return La solución aplicada creada con estado HTTP 201.
     */
    // Crear una solución aplicada
    @PostMapping
    public ResponseEntity<SolucionAplicada> crearSolucionAplicada(@RequestBody SolucionAplicada solucionAplicada) {
        SolucionAplicada nuevaSolucionAplicada = solucionAplicadaService.save(solucionAplicada);
        return new ResponseEntity<>(nuevaSolucionAplicada, HttpStatus.CREATED);
    }

    /**
     * Obtiene todas las soluciones aplicadas registradas.
     *
     * @return Lista de objetos SolucionAplicada.
     */
    // Obtener todas las soluciones aplicadas
    @GetMapping
    public ResponseEntity<List<SolucionAplicada>> obtenerTodasSolucionesAplicadas() {
        List<SolucionAplicada> soluciones = solucionAplicadaService.findAll();
        return new ResponseEntity<>(soluciones, HttpStatus.OK);
    }

    /**
     * Obtiene una solución aplicada por su ID.
     *
     * @param id ID de la solución aplicada.
     * @return Solución encontrada o HTTP 404 si no se encuentra.
     */
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

    /**
     * Actualiza una solución aplicada existente.
     *
     * @param id                ID de la solución aplicada a actualizar.
     * @param solucionAplicada  Datos actualizados.
     * @return Solución actualizada o HTTP 404 si no se encuentra.
     */
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

    /**
     * Agrega una nueva actividad a una solución aplicada.
     *
     * @param id        ID de la solución aplicada.
     * @param actividad Objeto Actividad a agregar.
     * @return Actividad creada con estado HTTP 201 o HTTP 404 si no se encuentra la solución.
     */
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

    /**
     * Actualiza el progreso de una actividad dentro de una solución aplicada.
     *
     * @param id         ID de la actividad.
     * @param completado Estado de completitud (true o false).
     * @return HTTP 204 si se actualiza correctamente, o HTTP 404 si no se encuentra.
     */
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

    /**
     * Elimina una solución aplicada por su ID.
     *
     * @param id ID de la solución aplicada a eliminar.
     * @return HTTP 204 si se elimina correctamente, o HTTP 404 si no se encuentra.
     */
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
