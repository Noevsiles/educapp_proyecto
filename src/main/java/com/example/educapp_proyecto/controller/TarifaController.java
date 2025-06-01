package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.model.Cliente;
import com.example.educapp_proyecto.model.Educador;
import com.example.educapp_proyecto.model.Tarifa;
import com.example.educapp_proyecto.repository.EducadorRepository;
import com.example.educapp_proyecto.security.JwtUtil;
import com.example.educapp_proyecto.service.impl.ClienteService;
import com.example.educapp_proyecto.service.impl.TarifaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Noelia Vázquez Siles
 * Controlador REST para gestionar las tarifas que los educadores asignan a sus servicios.
 */
@RestController
@RequestMapping("/api/tarifas")
public class TarifaController {
    @Autowired
    private TarifaService tarifaService;

    @Autowired
    private EducadorRepository educadorRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Crea una nueva tarifa asociada al educador autenticado.
     *
     * @param tarifa  Datos de la tarifa.
     * @param request Petición HTTP para extraer el token del educador.
     * @return Tarifa creada con estado HTTP 201.
     */
    // Crear una tarifa
    @PostMapping
    public ResponseEntity<Tarifa> crearTarifa(@RequestBody Tarifa tarifa, HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        Educador educador = educadorRepository.findByEmail(email).orElseThrow(() ->
                new RuntimeException("Educador no encontrado"));

        tarifa.setEducador(educador);
        Tarifa nuevaTarifa = tarifaService.save(tarifa);
        return new ResponseEntity<>(nuevaTarifa, HttpStatus.CREATED);
    }

    /**
     * Obtiene todas las tarifas del educador autenticado.
     *
     * @param request Petición HTTP con el token JWT del educador.
     * @return Lista de tarifas del educador.
     */
    // Obtener todas las tarifas del educador autenticado
    @GetMapping
    public ResponseEntity<List<Tarifa>> obtenerTarifasDelEducador(HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        Educador educador = educadorRepository.findByEmail(email).orElseThrow(() ->
                new RuntimeException("Educador no encontrado"));

        List<Tarifa> tarifas = tarifaService.findByEducador(educador);
        return ResponseEntity.ok(tarifas);
    }

    /**
     * Obtiene una tarifa por su ID, solo si pertenece al educador autenticado.
     *
     * @param id      ID de la tarifa.
     * @param request Petición HTTP con el token del educador.
     * @return Tarifa correspondiente o estado 401 si no pertenece al educador.
     */
    // Obtener una tarifa por ID (solo si pertenece al educador autenticado)
    @GetMapping("/{id}")
    public ResponseEntity<Tarifa> obtenerTarifaPorId(@PathVariable Long id, HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        Educador educador = educadorRepository.findByEmail(email).orElseThrow(() ->
                new RuntimeException("Educador no encontrado"));

        Tarifa tarifa = tarifaService.findById(id);
        if (!tarifa.getEducador().getIdEducador().equals(educador.getIdEducador())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(tarifa);
    }

    /**
     * Actualiza una tarifa existente si pertenece al educador autenticado.
     *
     * @param id                ID de la tarifa a actualizar.
     * @param tarifaActualizada Nuevos datos de la tarifa.
     * @param request           Petición HTTP con el token del educador.
     * @return Tarifa actualizada o estado 401 si no pertenece al educador.
     */
    // Actualizar tarifa si pertenece al educador autenticado
    @PutMapping("/{id}")
    public ResponseEntity<Tarifa> actualizarTarifa(@PathVariable Long id, @RequestBody Tarifa tarifaActualizada, HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        Educador educador = educadorRepository.findByEmail(email).orElseThrow(() ->
                new RuntimeException("Educador no encontrado"));

        Tarifa tarifaExistente = tarifaService.findById(id);
        if (!tarifaExistente.getEducador().getIdEducador().equals(educador.getIdEducador())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        tarifaExistente.setNombre(tarifaActualizada.getNombre());
        tarifaExistente.setDescripcion(tarifaActualizada.getDescripcion());
        tarifaExistente.setPrecio(tarifaActualizada.getPrecio());

        Tarifa actualizada = tarifaService.save(tarifaExistente);
        return ResponseEntity.ok(actualizada);
    }

    /**
     * Elimina una tarifa si pertenece al educador autenticado.
     *
     * @param id      ID de la tarifa a eliminar.
     * @param request Petición HTTP con el token del educador.
     * @return HTTP 204 si se elimina correctamente o 401 si no es autorizada.
     */
    // Eliminar tarifa solo si pertenece al educador autenticado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarifa(@PathVariable Long id, HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        Educador educador = educadorRepository.findByEmail(email).orElseThrow(() ->
                new RuntimeException("Educador no encontrado"));

        Tarifa tarifa = tarifaService.findById(id);
        if (!tarifa.getEducador().getIdEducador().equals(educador.getIdEducador())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        tarifaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene las tarifas del educador asignado al cliente autenticado.
     *
     * @param request Petición HTTP con el token del cliente.
     * @return Lista de tarifas del educador asignado o estado 404 si no tiene educador.
     */
    // Obtener tarifas por educador asignado al cliente
    @GetMapping("/cliente")
    public ResponseEntity<List<Tarifa>> obtenerTarifasDelEducadorAsignado(HttpServletRequest request) {
        String emailCliente = jwtUtil.extraerEmailDesdeRequest(request);
        Cliente cliente = clienteService.obtenerClientePorEmail(emailCliente);

        if (cliente == null || cliente.getEducador() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Tarifa> tarifas = tarifaService.findByEducadorId(cliente.getEducador().getIdEducador());
        return new ResponseEntity<>(tarifas, HttpStatus.OK);
    }
}
