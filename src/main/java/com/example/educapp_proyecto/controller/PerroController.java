package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.dto.PerroRequestDto;
import com.example.educapp_proyecto.dto.PerroResponseDto;
import com.example.educapp_proyecto.model.Perro;
import com.example.educapp_proyecto.service.BreedServiceInterface;
import com.example.educapp_proyecto.service.PerroServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perros")
public class PerroController {
    @Autowired
    private PerroServiceInterface perroService;

    @Autowired
    private BreedServiceInterface breedService;

    // Crear un perro
    @PostMapping
    public ResponseEntity<PerroResponseDto> crearPerro(@RequestBody PerroRequestDto dto) {
        PerroResponseDto nuevo = perroService.crearPerro(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // Obtener todos los perros
    @GetMapping
    public ResponseEntity<List<Perro>> obtenerTodosPerros() {
        List<Perro> perros = perroService.findAll();
        return new ResponseEntity<>(perros, HttpStatus.OK);
    }

    // Obtener un perro por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Perro> obtenerPerroPorId(@PathVariable Long id) {
        try {
            Perro perro = perroService.findById(id);
            return new ResponseEntity<>(perro, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar un perro
    @PutMapping("/{id}")
    public ResponseEntity<Perro> actualizarPerro(@PathVariable Long id, @RequestBody Perro perro) {
        try {
            Perro perroActualizado = perroService.updatePerro(id, perro);
            return new ResponseEntity<>(perroActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un perro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPerro(@PathVariable Long id) {
        try {
            perroService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Obtener todas las razas
    @GetMapping("/razas")
    public ResponseEntity<List<String>> getAllBreeds() {
        return ResponseEntity.ok(breedService.getAllBreeds());
    }

    // Obtener las imagenes de las razas
    @GetMapping("/razas/{breed}/imagenes")
    public ResponseEntity<List<String>> getBreedImages(@PathVariable String breed) {
        return ResponseEntity.ok(breedService.getBreedImages(breed));
    }
}
