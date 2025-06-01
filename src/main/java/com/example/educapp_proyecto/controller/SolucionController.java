package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.model.Solucion;
import com.example.educapp_proyecto.service.impl.SolucionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** @author Noelia Vázquez Siles
 * Controlador REST para la gestión de soluciones que pueden aplicarse a problemas de conducta.
 */
@RestController
@RequestMapping("/api/soluciones")
public class SolucionController {
    @Autowired
    private SolucionService solucionService;

    /**
     * Crea una nueva solución.
     *
     * @param solucion Objeto Solucion con los datos a registrar.
     * @return La solución creada con estado HTTP 201.
     */
    // Crear una solución
    @PostMapping
    public ResponseEntity<Solucion> crearSolucion(@RequestBody Solucion solucion) {
        Solucion nuevaSolucion = solucionService.save(solucion);
        return new ResponseEntity<>(nuevaSolucion, HttpStatus.CREATED);
    }

    /**
     * Obtiene todas las soluciones disponibles.
     *
     * @return Lista de objetos {@link Solucion} con estado HTTP 200.
     */
    // Obtener todas las soluciones
    @GetMapping
    public ResponseEntity<List<Solucion>> obtenerTodasLasSoluciones() {
        List<Solucion> soluciones = solucionService.findAll();
        return new ResponseEntity<>(soluciones, HttpStatus.OK);
    }

    /**
     * Obtiene una solución específica por su ID.
     *
     * @param id ID de la solución.
     * @return Solución correspondiente o estado HTTP 404 si no se encuentra.
     */
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

    /**
     * Actualiza una solución existente.
     *
     * @param id       ID de la solución a actualizar.
     * @param solucion Datos nuevos para actualizar.
     * @return Solución actualizada o estado HTTP 404 si no se encuentra.
     */
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

    /**
     * Elimina una solución por su ID.
     *
     * @param id ID de la solución a eliminar.
     * @return HTTP 204 si se elimina correctamente, o HTTP 404 si no existe.
     */
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
