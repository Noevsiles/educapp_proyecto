package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.model.Causa;
import com.example.educapp_proyecto.service.impl.CausaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Noelia Vázquez Siles
 * Controlador REST para la gestión de causas asociadas a problemas de conducta.
 */
@RestController
@RequestMapping("/api/causas")
public class CausaController {
    @Autowired
    private CausaService causaService;

    /**
     * Crea una nueva causa.
     *
     * @param causa Objeto {@link Causa} que se desea crear.
     * @return La causa creada con estado HTTP 201.
     */
    // Crear una causa
    @PostMapping
    public ResponseEntity<Causa> crearCausa(@RequestBody Causa causa) {
        Causa nuevaCausa = causaService.save(causa);
        return new ResponseEntity<>(nuevaCausa, HttpStatus.CREATED);
    }

    /**
     * Obtiene todas las causas registradas en el sistema.
     *
     * @return Lista de causas con estado HTTP 200.
     */
    // Obtener todas las causas
    @GetMapping
    public ResponseEntity<List<Causa>> obtenerTodasLasCausas() {
        List<Causa> causas = causaService.findAll();
        return new ResponseEntity<>(causas, HttpStatus.OK);
    }

    /**
     * Obtiene una causa específica por su ID.
     *
     * @param id ID de la causa a consultar.
     * @return La causa encontrada con estado HTTP 200, o HTTP 404 si no existe.
     */
    // Obtener una causa por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Causa> obtenerCausaPorId(@PathVariable Long id) {
        try {
            Causa causa = causaService.findById(id);
            return new ResponseEntity<>(causa, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Actualiza una causa existente por su ID.
     *
     * @param id    ID de la causa a actualizar.
     * @param causa Datos nuevos de la causa.
     * @return La causa actualizada con estado HTTP 200, o HTTP 404 si no existe.
     */
    // Actualizar una causa
    @PutMapping("/{id}")
    public ResponseEntity<Causa> actualizarCausa(@PathVariable Long id, @RequestBody Causa causa) {
        try {
            Causa causaActualizada = causaService.updateCausa(id, causa);
            return new ResponseEntity<>(causaActualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina una causa por su ID.
     *
     * @param id ID de la causa a eliminar.
     * @return HTTP 204 si la eliminación fue exitosa, o HTTP 404 si la causa no existe.
     */
    // Eliminar una causa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCausa(@PathVariable Long id) {
        try {
            causaService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
