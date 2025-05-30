package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.dto.ReservaSesionDto;
import com.example.educapp_proyecto.dto.SesionRequestDto;
import com.example.educapp_proyecto.dto.SesionResponseDto;
import com.example.educapp_proyecto.model.Sesion;
import com.example.educapp_proyecto.repository.SesionRepository;
import com.example.educapp_proyecto.security.JwtUtil;
import com.example.educapp_proyecto.service.impl.SesionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sesiones")
public class SesionController {

    @Autowired
    private SesionService sesionService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SesionRepository sesionRepository;

    // Crear una sesión
    @PostMapping
    public ResponseEntity<Sesion> crearSesion(@RequestBody SesionRequestDto dto) {
        Sesion nueva = sesionService.crearSesion(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // Obtener todas las sesiones
    @GetMapping
    public ResponseEntity<List<Sesion>> obtenerTodasLasSesiones() {
        List<Sesion> sesiones = sesionService.findAll();
        return new ResponseEntity<>(sesiones, HttpStatus.OK);
    }

    // Obtener sesiones del educador
    @GetMapping("/educador")
    public ResponseEntity<List<SesionResponseDto>> obtenerSesionesDelEducador(HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        List<SesionResponseDto> sesiones = sesionService.obtenerSesionesPorEducador(email);
        return ResponseEntity.ok(sesiones);
    }


    // Obtener una sesión por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Sesion> obtenerSesionPorId(@PathVariable Long id) {
        try {
            Sesion sesion = sesionService.findById(id);
            return new ResponseEntity<>(sesion, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar una sesión
    @PutMapping("/{id}")
    public ResponseEntity<Sesion> actualizarSesion(@PathVariable Long id, @RequestBody Sesion sesion) {
        try {
            Sesion sesionActualizada = sesionService.save(sesion);
            return new ResponseEntity<>(sesionActualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar una sesión
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSesion(@PathVariable Long id) {
        try {
            sesionService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Reservar una sesión
    @PostMapping("/reservar")
    public ResponseEntity<Sesion> reservar(@RequestBody ReservaSesionDto dto, HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        Sesion sesion = sesionService.reservarSesion(dto, email);
        return ResponseEntity.ok(sesion);
    }

    //Marcar sesion como realizada
    @PatchMapping("/{id}/realizar")
    public ResponseEntity<Sesion> marcarSesionComoRealizada(@PathVariable Long id) {
        Sesion sesionActualizada = sesionService.marcarComoRealizada(id);
        return ResponseEntity.ok(sesionActualizada);
    }

    //Filtrar las sesiones por cliente, perro o educador
    @GetMapping("/filtrar")
    public ResponseEntity<List<Sesion>> filtrarSesiones(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) Long perroId,
            @RequestParam(required = false) Long educadorId
    ) {
        List<Sesion> resultado = sesionService.filtrarSesiones(clienteId, perroId, educadorId);
        return ResponseEntity.ok(resultado);
    }

    //Enviar recordatorios de sesiones
    @PostMapping("/enviar-recordatorios")
    public ResponseEntity<String> enviarRecordatorios() {
        sesionService.enviarRecordatorios();
        return ResponseEntity.ok("Recordatorios enviados correctamente");
    }

    // Obtener agenda del educador
    @GetMapping("/educador/{id}/agenda")
    public ResponseEntity<List<Sesion>> obtenerAgenda(@PathVariable Long id) {
        List<Sesion> sesiones = sesionRepository.findByEducador_IdEducador(id);
        return ResponseEntity.ok(sesiones);
    }

    // Aceptar sesion
    @PutMapping("/{id}/aceptar")
    public ResponseEntity<Void> aceptarSesion(@PathVariable Long id) {
        sesionService.aceptarSesion(id);
        return ResponseEntity.ok().build();
    }
}
