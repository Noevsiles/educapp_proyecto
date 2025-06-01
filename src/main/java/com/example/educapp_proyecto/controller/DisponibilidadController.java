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

/**
 * @author Noelia Vázquez Siles
 * Controlador REST para gestionar la disponibilidad semanal de los educadores.
 */
@RestController
@RequestMapping("/api/disponibilidad")
public class DisponibilidadController {

    @Autowired
    private DisponibilidadService disponibilidadService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Crea o actualiza la disponibilidad semanal del educador autenticado.
     *
     * @param dto     Objeto DisponibilidadRequestDto con los datos de disponibilidad.
     * @param request Petición HTTP para extraer el email desde el token JWT.
     * @return Mensaje de éxito si la disponibilidad se guarda correctamente.
     */
    // Crear o actualizar la disponibilidad del educador
    @PostMapping
    public ResponseEntity<String> guardarDisponibilidad(@RequestBody DisponibilidadRequestDto dto, HttpServletRequest request) {

        String email = jwtUtil.extraerEmailDesdeRequest(request);
        disponibilidadService.guardarDisponibilidad(dto, email);
        return ResponseEntity.ok("Disponibilidad guardada correctamente");
    }

    /**
     * Elimina una entrada de disponibilidad específica por su ID.
     *
     * @param id ID de la disponibilidad a eliminar.
     * @return HTTP 204 si la eliminación fue exitosa.
     */
    // Eliminar una disponibilidad por su id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        disponibilidadService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Actualiza una entrada de disponibilidad existente.
     *
     * @param id      ID de la disponibilidad a actualizar.
     * @param dto     Objeto con los nuevos datos.
     * @param request Petición HTTP para extraer el email desde el token JWT.
     * @return HTTP 200 si la actualización fue exitosa.
     */
    // Actualizar una disponibilidad
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizar(@PathVariable Long id, @RequestBody DisponibilidadRequestDto dto, HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        disponibilidadService.actualizar(id, dto, email);
        return ResponseEntity.ok().build();
    }

    /**
     * Obtiene la disponibilidad semanal de un educador específico por su ID.
     *
     * @param educadorId ID del educador.
     * @return Lista de objetos DisponibilidadResponseDto con los horarios del educador.
     */
    // Obtener disponibilidad del educador
    @GetMapping("/{educadorId}")
    public ResponseEntity<List<DisponibilidadResponseDto>> obtenerDisponibilidad(@PathVariable Long educadorId) {
        List<DisponibilidadResponseDto> disponibilidad = disponibilidadService.obtenerPorEducador(educadorId);
        return ResponseEntity.ok(disponibilidad);
    }

    /**
     * Obtiene la disponibilidad del educador autenticado, usando su email extraído del token.
     *
     * @param request Petición HTTP para extraer el email desde el token JWT.
     * @return Lista de horarios disponibles del educador.
     */
    // Obtener disponibilidad del educador por su email
    @GetMapping("/educador")
    public ResponseEntity<List<DisponibilidadResponseDto>> obtenerDisponibilidadDelEducador(HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        List<DisponibilidadResponseDto> disponibilidad = disponibilidadService.obtenerPorEmailEducador(email);
        return ResponseEntity.ok(disponibilidad);
    }
}

