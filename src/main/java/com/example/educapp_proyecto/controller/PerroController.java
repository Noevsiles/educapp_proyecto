package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.dto.PerroDetalleDto;
import com.example.educapp_proyecto.dto.PerroRequestDto;
import com.example.educapp_proyecto.dto.PerroResponseDto;
import com.example.educapp_proyecto.model.Perro;
import com.example.educapp_proyecto.security.JwtUtil;
import com.example.educapp_proyecto.service.BreedServiceInterface;
import com.example.educapp_proyecto.service.PerroServiceInterface;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/** @author Noelia Vázquez Siles
 * Controlador REST para gestionar los perros, incluyendo operaciones CRUD,
 * asignación de problemas y consulta por educador o cliente.
 */
@RestController
@RequestMapping("/api/perros")
public class PerroController {
    @Autowired
    private PerroServiceInterface perroService;

    @Autowired
    private BreedServiceInterface breedService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Crea un nuevo perro.
     *
     * @param dto Datos del perro a crear.
     * @return DTO del perro creado con estado HTTP 201.
     */
    // Crear un perro
    @PostMapping
    public ResponseEntity<PerroResponseDto> crearPerro(@RequestBody PerroRequestDto dto) {
        PerroResponseDto nuevo = perroService.crearPerro(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    /**
     * Obtiene todos los perros registrados.
     *
     * @return Lista de perros en formato DTO.
     */
    // Obtener todos los perros
    @GetMapping
    public ResponseEntity<List<PerroResponseDto>> obtenerTodosPerros() {
        List<Perro> perros = perroService.findAll();
        List<PerroResponseDto> dtos = perros.stream()
                .map(perroService::convertirAPerroDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Obtiene un perro por su ID.
     *
     * @param id ID del perro.
     * @return Entidad Perro o HTTP 404 si no se encuentra.
     */
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

    /**
     * Actualiza los datos de un perro existente.
     *
     * @param id  ID del perro.
     * @param dto datos del perro.
     * @return DTO actualizado o HTTP 404 si no se encuentra.
     */
    // Actualizar un perro
    @PutMapping("/{id}")
    public ResponseEntity<PerroResponseDto> actualizarPerro(@PathVariable Long id, @RequestBody PerroRequestDto dto) {
        try {
            PerroResponseDto actualizado = perroService.actualizarPerroDesdeDto(id, dto);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina un perro por su ID.
     *
     * @param id ID del perro.
     * @return HTTP 204 si se elimina correctamente o 404 si no existe.
     */
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

    /**
     * Obtiene una lista de todas las razas disponibles en la api dog.ceo.
     *
     * @return Lista de nombres de razas.
     */
    // Obtener todas las razas
    @GetMapping("/razas")
    public ResponseEntity<List<String>> getAllBreeds() {
        return ResponseEntity.ok(breedService.getAllBreeds());
    }

    /**
     * Obtiene imágenes representativas de una raza específica de la api dog.ceo.
     *
     * @param breed Nombre de la raza.
     * @return Lista de URLs de imágenes.
     */
    // Obtener las imagenes de las razas
    @GetMapping("/razas/{breed}/imagenes")
    public ResponseEntity<List<String>> getBreedImages(@PathVariable String breed) {
        return ResponseEntity.ok(breedService.getBreedImages(breed));
    }

    /**
     * Obtiene los perros registrados por el educador autenticado.
     *
     * @param request Petición HTTP para extraer el token JWT.
     * @return Lista de perros asociados al educador.
     */
    // Obtener los perros asociados al educador
    @GetMapping("/educador")
    public ResponseEntity<List<PerroResponseDto>> obtenerPerrosEducador(HttpServletRequest request) {
        String email = jwtUtil.extraerEmailDesdeRequest(request);
        List<PerroResponseDto> perros = perroService.obtenerPerrosPorEducador(email);
        return ResponseEntity.ok(perros);
    }

    /**
     * Asigna problemas de conducta a un perro.
     *
     * @param id           ID del perro.
     * @param idsProblemas Lista de IDs de los problemas a asignar.
     * @return HTTP 200 si la asignación fue exitosa.
     */
    // Asignar problemas a perro
    @PutMapping("/{id}/problemas")
    public ResponseEntity<?> asignarProblemas(
            @PathVariable Long id,
            @RequestBody List<Long> idsProblemas) {
        perroService.asignarProblemasA(id, idsProblemas);
        return ResponseEntity.ok().build();
    }

    /**
     * Obtiene el detalle de un perro, incluyendo sus problemas de conducta.
     *
     * @param id ID del perro.
     * @return DTO con el detalle completo del perro.
     */
    // Obtener perros con problemas
    @GetMapping("/{id}/detalle")
    public ResponseEntity<PerroDetalleDto> obtenerDetalle(@PathVariable Long id) {
        Perro perro = perroService.findById(id);
        return ResponseEntity.ok(perroService.convertirADetalleDto(perro));
    }

    /**
     * Obtiene todos los perros asociados al cliente autenticado.
     *
     * @param request Petición HTTP para extraer el token JWT.
     * @return Lista de perros del cliente.
     */
    // Obtener los perros del cliente (puede tener mas de uno)
    @GetMapping("/cliente")
    public ResponseEntity<List<PerroResponseDto>> obtenerPerrosDelCliente(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String emailCliente = jwtUtil.extraerEmail(token);
        List<PerroResponseDto> perros = perroService.obtenerPerrosPorCliente(emailCliente);
        return ResponseEntity.ok(perros);
    }
}
