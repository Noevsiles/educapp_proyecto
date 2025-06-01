package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.model.CausaDeProblema;
import com.example.educapp_proyecto.service.impl.CausaDeProblemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** @author Noelia Vázquez Siles
 * Controlador REST para la gestión de las causas asociadas a problemas de conducta en perros.
 */
@RestController
@RequestMapping("/api/causas-de-problema")
public class CausaDeProblemaController {
    @Autowired
    private CausaDeProblemaService causaDeProblemaService;

    /**
     * Crea una nueva causa de problema.
     *
     * @param causaDeProblema Objeto CausaDeProblema que se desea registrar.
     * @return La causa creada con estado HTTP 201.
     */
    // Crear una causa de problema
    @PostMapping
    public ResponseEntity<CausaDeProblema> crearCausaDeProblema(@RequestBody CausaDeProblema causaDeProblema) {
        CausaDeProblema nuevaCausaDeProblema = causaDeProblemaService.save(causaDeProblema);
        return new ResponseEntity<>(nuevaCausaDeProblema, HttpStatus.CREATED);
    }

    /**
     * Obtiene todas las causas de problema registradas.
     *
     * @return Lista de objetos CausaDeProblema con estado HTTP 200.
     */
    // Obtener todas las causas de problemas
    @GetMapping
    public ResponseEntity<List<CausaDeProblema>> obtenerTodasLasCausasDeProblema() {
        List<CausaDeProblema> causas = causaDeProblemaService.findAll();
        return new ResponseEntity<>(causas, HttpStatus.OK);
    }

    /**
     * Obtiene una causa de problema específica por su ID.
     *
     * @param id ID de la causa de problema a consultar.
     * @return La causa encontrada con estado HTTP 200, o HTTP 404 si no existe.
     */
    // Obtener una causa de problema por su ID
    @GetMapping("/{id}")
    public ResponseEntity<CausaDeProblema> obtenerCausaDeProblemaPorId(@PathVariable Long id) {
        try {
            CausaDeProblema causaDeProblema = causaDeProblemaService.findById(id);
            return new ResponseEntity<>(causaDeProblema, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Actualiza una causa de problema existente.
     *
     * @param id                ID de la causa a actualizar.
     * @param causaDeProblema   Datos actualizados de la causa.
     * @return La causa actualizada con estado HTTP 200, o HTTP 404 si no se encuentra.
     */
    // Actualizar una causa de problema
    @PutMapping("/{id}")
    public ResponseEntity<CausaDeProblema> actualizarCausaDeProblema(@PathVariable Long id, @RequestBody CausaDeProblema causaDeProblema) {
        try {
            CausaDeProblema causaDeProblemaActualizada = causaDeProblemaService.updateCausaDeProblema(id, causaDeProblema);
            return new ResponseEntity<>(causaDeProblemaActualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina una causa de problema por su ID.
     *
     * @param id ID de la causa de problema a eliminar.
     * @return HTTP 204 si se elimina correctamente, o HTTP 404 si no se encuentra.
     */
    // Eliminar una causa de problema
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCausaDeProblema(@PathVariable Long id) {
        try {
            causaDeProblemaService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
