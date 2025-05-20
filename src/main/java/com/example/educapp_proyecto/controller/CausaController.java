package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.model.Causa;
import com.example.educapp_proyecto.service.impl.CausaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/causas")
public class CausaController {
    @Autowired
    private CausaService causaService;

    // Crear una causa
    @PostMapping
    public ResponseEntity<Causa> crearCausa(@RequestBody Causa causa) {
        Causa nuevaCausa = causaService.save(causa);
        return new ResponseEntity<>(nuevaCausa, HttpStatus.CREATED);
    }

    // Obtener todas las causas
    @GetMapping
    public ResponseEntity<List<Causa>> obtenerTodasLasCausas() {
        List<Causa> causas = causaService.findAll();
        return new ResponseEntity<>(causas, HttpStatus.OK);
    }

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
