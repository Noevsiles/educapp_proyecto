package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.dto.*;
import com.example.educapp_proyecto.model.DiaSemana;
import com.example.educapp_proyecto.model.Sesion;
import com.example.educapp_proyecto.repository.SesionRepository;
import com.example.educapp_proyecto.security.JwtUtil;
import com.example.educapp_proyecto.service.impl.SesionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Noelia Vázquez Siles
 * Controlador REST para la gestión de sesiones de adiestramiento.
 */
@RestController
@RequestMapping("/api/sesiones")
public class SesionController {

    @Autowired
    private SesionService sesionService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SesionRepository sesionRepository;

    /**
     * Crea una nueva sesión.
     *
     * @param dto DTO con los datos de la sesión.
     * @return Sesión creada con estado HTTP 201.
     */
    // Crear una sesión
    @PostMapping
    public ResponseEntity<Sesion> crearSesion(@RequestBody SesionRequestDto dto) {
        Sesion nueva = sesionService.crearSesion(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    /**
     * Obtiene todas las sesiones registradas.
     *
     * @return Lista completa de sesiones.
     */
    // Obtener todas las sesiones
    @GetMapping
    public ResponseEntity<List<Sesion>> obtenerTodasLasSesiones() {
        List<Sesion> sesiones = sesionService.findAll();
        return new ResponseEntity<>(sesiones, HttpStatus.OK);
    }

    /**
     * Obtiene las sesiones asociadas al educador autenticado.
     *
     * @param request Petición HTTP con el token.
     * @return Lista de sesiones del educador.
     */
    // Obtener sesiones del educador
    @GetMapping("/educador")
    public ResponseEntity<List<SesionResponseDto>> obtenerSesionesDelEducador(HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        List<SesionResponseDto> sesiones = sesionService.obtenerSesionesPorEducador(email);
        return ResponseEntity.ok(sesiones);
    }


    /**
     * Obtiene una sesión por su ID.
     *
     * @param id ID de la sesión.
     * @return Sesión correspondiente o estado 404 si no se encuentra.
     */
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

    /**
     * Actualiza una sesión existente.
     *
     * @param id     ID de la sesión.
     * @param sesion Objeto con los nuevos datos.
     * @return Sesión actualizada o estado 404 si no se encuentra.
     */
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

    /**
     * Elimina una sesión por su ID.
     *
     * @param id ID de la sesión.
     * @return HTTP 204 si se elimina correctamente, o 404 si no se encuentra.
     */
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

    /**
     * Reserva una sesión desde la interfaz del cliente.
     *
     * @param dto     Datos para la reserva.
     * @param request Petición HTTP con token del cliente.
     * @return Sesión reservada.
     */
    //Reservar una sesión
    @PostMapping("/reservar")
    public ResponseEntity<Sesion> reservar(@RequestBody ReservaSesionDto dto, HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        Sesion sesion = sesionService.reservarSesion(dto, email);
        return ResponseEntity.ok(sesion);
    }

    /**
     * Marca una sesión como realizada.
     *
     * @param id ID de la sesión.
     * @return Sesión actualizada como realizada.
     */
    //Marcar sesion como realizada
    @PatchMapping("/{id}/realizar")
    public ResponseEntity<Sesion> marcarSesionComoRealizada(@PathVariable Long id) {
        Sesion sesionActualizada = sesionService.marcarComoRealizada(id);
        return ResponseEntity.ok(sesionActualizada);
    }

    /**
     * Filtra sesiones por cliente, perro o educador.
     *
     * @param clienteId  ID del cliente (opcional).
     * @param perroId    ID del perro (opcional).
     * @param educadorId ID del educador (opcional).
     * @return Lista de sesiones filtradas.
     */
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

    /**
     * Envía recordatorios por email para las sesiones próximas.
     *
     * @return Mensaje de confirmación.
     */
    //Enviar recordatorios de sesiones
    @PostMapping("/enviar-recordatorios")
    public ResponseEntity<String> enviarRecordatorios() {
        sesionService.enviarRecordatorios();
        return ResponseEntity.ok("Recordatorios enviados correctamente");
    }

    /**
     * Obtiene la agenda completa de un educador.
     *
     * @param id ID del educador.
     * @return Lista de sesiones agendadas.
     */
    // Obtener agenda del educador
    @GetMapping("/educador/{id}/agenda")
    public ResponseEntity<List<Sesion>> obtenerAgenda(@PathVariable Long id) {
        List<Sesion> sesiones = sesionRepository.findByEducador_IdEducador(id);
        return ResponseEntity.ok(sesiones);
    }

    /**
     * Acepta una sesión pendiente.
     *
     * @param id ID de la sesión.
     * @return HTTP 200 si se acepta correctamente.
     */
    // Aceptar sesion
    @PutMapping("/{id}/aceptar")
    public ResponseEntity<Void> aceptarSesion(@PathVariable Long id) {
        sesionService.aceptarSesion(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Rechaza una sesión pendiente.
     *
     * @param id ID de la sesión.
     * @return DTO con la sesión rechazada.
     */
    // Rechazar sesion
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<SesionResponseDto> rechazarSesion(@PathVariable Long id) {
        return ResponseEntity.ok(sesionService.rechazarSesion(id));
    }


    /**
     * Obtiene los huecos disponibles en la agenda de un educador para una fecha específica.
     *
     * @param educadorId ID del educador.
     * @param fecha      Fecha en formato YYYY-MM-DD.
     * @return Lista de horas disponibles.
     */
    // Obtener disponibilidad del educador
    @GetMapping("/disponibles")
    public ResponseEntity<List<String>> obtenerDisponibilidad(
            @RequestParam Long educadorId,
            @RequestParam String fecha
    ) {
        LocalDate fechaLocal = LocalDate.parse(fecha);

        List<String> disponibles = sesionService.obtenerHuecosDisponibles(educadorId, fechaLocal);
        return ResponseEntity.ok(disponibles);
    }

    /**
     * Obtiene las sesiones del cliente autenticado.
     *
     * @param request Petición HTTP con token del cliente.
     * @return Lista de sesiones en formato DTO.
     */
    // Obtener las sesiones del cliente
    @GetMapping("/cliente")
    public ResponseEntity<List<SesionClienteResponseDto>> obtenerSesionesDelCliente(HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        List<SesionClienteResponseDto> sesiones = sesionService.obtenerSesionesPorCliente(email);
        return ResponseEntity.ok(sesiones);
    }
}
