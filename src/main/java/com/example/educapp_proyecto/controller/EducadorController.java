package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.model.Educador;
import com.example.educapp_proyecto.model.Usuario;
import com.example.educapp_proyecto.repository.EducadorRepository;
import com.example.educapp_proyecto.repository.UsuarioRepository;
import com.example.educapp_proyecto.security.JwtUtil;
import com.example.educapp_proyecto.service.impl.EducadorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/** @author Noelia Vázquez Siles
 * Controlador REST para la gestión de educadores caninos en el sistema.
 */
@RestController
@RequestMapping("/api/educador")
public class EducadorController {
    @Autowired
    private EducadorService educadorService;

    @Autowired
    private JwtUtil jwtUtil;


    /**
     * Crea un nuevo educador.
     *
     * @param educador Objeto Educador con los datos del nuevo educador.
     * @return Educador creado con estado HTTP 201 (Created).
     */
    // Crear un educador
    @PostMapping
    public ResponseEntity<Educador> crearEducador(@RequestBody Educador educador, HttpServletRequest request) {
        try {
            String email = jwtUtil.extraerEmailDesdeRequest(request);
            Educador nuevoEducador = educadorService.crearEducadorParaUsuario(educador, email);
            return new ResponseEntity<>(nuevoEducador, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/perfil")
    public ResponseEntity<Educador> crearOActualizarPerfilEducador(@RequestBody Educador educador,
                                                                   HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        Educador actualizado = educadorService.crearOActualizarPerfilEducador(educador, email);
        return ResponseEntity.ok(actualizado);
    }


    /**
     * Obtiene todos los educadores registrados.
     *
     * @return Lista de educadores con estado HTTP 200 (OK).
     */
    // Obtener todos los educadores
    @GetMapping
    public ResponseEntity<List<Educador>> obtenerTodosEducadores() {
        List<Educador> educadores = educadorService.findAll();
        return new ResponseEntity<>(educadores, HttpStatus.OK);
    }

    /**
     * Obtiene un educador específico por su ID.
     *
     * @param id ID del educador a consultar.
     * @return Educador encontrado o estado HTTP 404 si no existe.
     */
    // Obtener un educador por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Educador> obtenerEducadorPorId(@PathVariable Long id) {
        try {
            Educador educador = educadorService.findById(id);
            return new ResponseEntity<>(educador, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/email")
    public ResponseEntity<Educador> obtenerEducadorPorEmail(HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        Optional<Educador> educadorOpt = educadorService.obtenerEducadorPorEmailDesdeToken(email);

        return educadorOpt
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    /**
     * Actualiza los datos de un educador existente.
     *
     * @param id       ID del educador a actualizar.
     * @param educador Objeto Educador con los datos actualizados.
     * @return Educador actualizado o estado HTTP 404 si no se encuentra.
     */
    // Actualizar un educador
    @PutMapping("/{id}")
    public ResponseEntity<Educador> actualizarEducador(@PathVariable Long id, @RequestBody Educador educador) {
        try {
            Educador educadorActualizado = educadorService.updateEducador(id, educador);
            return new ResponseEntity<>(educadorActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina un educador por su ID.
     *
     * @param id ID del educador a eliminar.
     * @return HTTP 204 si se elimina correctamente, o HTTP 404 si no se encuentra.
     */
    // Eliminar un educador
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEducador(@PathVariable Long id) {
        try {
            educadorService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
