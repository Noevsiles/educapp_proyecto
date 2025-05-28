package com.example.educapp_proyecto.controller;

import com.example.educapp_proyecto.dto.ActividadAsignadaRequestDto;
import com.example.educapp_proyecto.dto.ActividadAsignadaResponseDto;
import com.example.educapp_proyecto.service.ActividadAsignadaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividades-asignadas")
public class ActividadAsignadaController {

    @Autowired
    private ActividadAsignadaServiceInterface service;

    @PostMapping
    public ResponseEntity<Void> asignarActividad(@RequestBody ActividadAsignadaRequestDto dto) {
        service.asignarActividad(dto.getActividadId(), dto.getPerroId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/perro/{perroId}")
    public List<ActividadAsignadaResponseDto> getActividadesPorPerro(@PathVariable Long perroId) {
        return service.obtenerActividadesPorPerro(perroId);
    }

    @PatchMapping("/{id}/completar")
    public ResponseEntity<Void> marcarComoCompletada(@PathVariable Long id) {
        service.marcarComoCompletada(id);
        return ResponseEntity.ok().build();
    }
}
