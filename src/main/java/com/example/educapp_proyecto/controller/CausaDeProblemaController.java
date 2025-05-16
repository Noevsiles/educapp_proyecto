package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.model.CausaDeProblema;
import com.example.educapp_proyecto.service.impl.CausaDeProblemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/causas-de-problema")
public class CausaDeProblemaController {
    @Autowired
    private CausaDeProblemaService causaDeProblemaService;

    // Crear una causa de problema
    @PostMapping
    public ResponseEntity<CausaDeProblema> crearCausaDeProblema(@RequestBody CausaDeProblema causaDeProblema) {
        CausaDeProblema nuevaCausaDeProblema = causaDeProblemaService.save(causaDeProblema);
        return new ResponseEntity<>(nuevaCausaDeProblema, HttpStatus.CREATED);
    }

    // Obtener todas las causas de problemas
    @GetMapping
    public ResponseEntity<List<CausaDeProblema>> obtenerTodasLasCausasDeProblema() {
        List<CausaDeProblema> causas = causaDeProblemaService.findAll();
        return new ResponseEntity<>(causas, HttpStatus.OK);
    }

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
