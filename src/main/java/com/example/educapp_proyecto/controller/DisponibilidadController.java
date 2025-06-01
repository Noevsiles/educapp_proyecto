package com.example.educapp_proyecto.controller;

import com.example.educapp_proyecto.dto.DisponibilidadRequestDto;
import com.example.educapp_proyecto.dto.DisponibilidadResponseDto;
import com.example.educapp_proyecto.security.JwtUtil;
import com.example.educapp_proyecto.service.impl.DisponibilidadService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disponibilidad")
public class DisponibilidadController {

    @Autowired
    private DisponibilidadService disponibilidadService;

    @Autowired
    private JwtUtil jwtUtil;

    // Crear o actualizar la disponibilidad del educador
    @PostMapping
    public ResponseEntity<String> guardarDisponibilidad(@RequestBody DisponibilidadRequestDto dto, HttpServletRequest request) {

        String email = jwtUtil.extraerEmailDesdeRequest(request);
        disponibilidadService.guardarDisponibilidad(dto, email);
        return ResponseEntity.ok("Disponibilidad guardada correctamente");
    }

    // Eliminar una disponibilidad por su id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        disponibilidadService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    // Actualizar una disponibilidad
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(@PathVariable Long id, @RequestBody DisponibilidadRequestDto dto, HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        disponibilidadService.actualizar(id, dto, email);
        return ResponseEntity.ok().build();
    }

    // Obtener disponibilidad del educador
    @GetMapping("/{educadorId}")
    public ResponseEntity<List<DisponibilidadResponseDto>> obtenerDisponibilidad(@PathVariable Long educadorId) {
        List<DisponibilidadResponseDto> disponibilidad = disponibilidadService.obtenerPorEducador(educadorId);
        return ResponseEntity.ok(disponibilidad);
    }

    // Obtener disponibilidad del educador por su email
    @GetMapping("/educador")
    public ResponseEntity<List<DisponibilidadResponseDto>> obtenerDisponibilidadDelEducador(HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        List<DisponibilidadResponseDto> disponibilidad = disponibilidadService.obtenerPorEmailEducador(email);
        return ResponseEntity.ok(disponibilidad);
    }


}

