package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.dto.PlanTrabajoClienteDto;
import com.example.educapp_proyecto.dto.PlanTrabajoDto;
import com.example.educapp_proyecto.dto.PlanTrabajoRespuestaDto;
import com.example.educapp_proyecto.model.PlanTrabajo;
import com.example.educapp_proyecto.security.JwtUtil;
import com.example.educapp_proyecto.service.PlanTrabajoServiceInterface;
import com.example.educapp_proyecto.service.impl.PlanTrabajoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Noelia Vázquez Siles
 * Controlador REST para la gestión de planes de trabajo asociados a perros, clientes y educadores.
 */
@RestController
@RequestMapping("/api/planes")
public class PlanTrabajoController {

    @Autowired
    private PlanTrabajoServiceInterface planService;

    @Autowired
    private PlanTrabajoService planTrabajoService;

    @Autowired
    private JwtUtil jwtUtil;


    /**
     * Crea un nuevo plan de trabajo a partir de un DTO.
     *
     * @param dto DTO con la información del plan.
     * @return Mensaje de éxito si el plan fue creado correctamente.
     */
    @PostMapping
    public ResponseEntity<String> crear(@RequestBody PlanTrabajoDto dto) {
        planTrabajoService.crearPlan(dto);
        return ResponseEntity.ok("Plan creado con éxito");
    }

    /**
     * Obtiene todos los planes de trabajo asociados a un cliente específico.
     *
     * @param clienteId ID del cliente.
     * @return Lista de planes en formato {@link PlanTrabajoRespuestaDto}.
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PlanTrabajoRespuestaDto>> porCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(planService.listarPlanesPorCliente(clienteId));
    }

    /**
     * Busca un plan de trabajo por su ID.
     *
     * @param id ID del plan de trabajo.
     * @return El plan de trabajo encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlanTrabajo> porId(@PathVariable Long id) {
        return ResponseEntity.ok(planService.buscarPorId(id));
    }


    /**
     * Obtiene todos los planes de trabajo del sistema.
     *
     * @return Lista completa de entidades PlanTrabajo.
     */
    @GetMapping("/todos")
    public ResponseEntity<List<PlanTrabajo>> obtenerTodos() {
        List<PlanTrabajo> planes = planTrabajoService.obtenerTodos();
        return ResponseEntity.ok(planes);
    }

    /**
     * Guarda un plan de trabajo directamente desde la entidad.
     *
     * @param plan Objeto PlanTrabajo a guardar.
     * @return El plan creado con estado HTTP 201.
     */
    @PostMapping("/save")
    public ResponseEntity<PlanTrabajo> save(@RequestBody PlanTrabajo plan) {
        PlanTrabajo creado = planTrabajoService.save(plan);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creado);
    }

    /**
     * Elimina un plan de trabajo por su ID.
     *
     * @param id ID del plan a eliminar.
     * @return HTTP 204 si la eliminación fue exitosa.
     */
    // Eliminar un plan de trabajo por su id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id) {
        planTrabajoService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene los planes de trabajo del cliente autenticado mediante JWT.
     *
     * @param request Petición HTTP para extraer el token.
     * @return Lista de planes del cliente autenticado.
     */
    @GetMapping("/cliente")
    public ResponseEntity<List<PlanTrabajoClienteDto>> obtenerPlanesDelCliente(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String emailCliente = jwtUtil.extraerEmail(token);
        List<PlanTrabajoClienteDto> planes = planTrabajoService.obtenerPlanesPorCliente(emailCliente);
        return ResponseEntity.ok(planes);
    }

    /**
     * Obtiene los planes de trabajo del educador autenticado mediante JWT.
     *
     * @param request Petición HTTP para extraer el token.
     * @return Lista de planes en formato {@link PlanTrabajoRespuestaDto}.
     */
    @GetMapping("/educador")
    public ResponseEntity<List<PlanTrabajoRespuestaDto>> obtenerPlanesDelEducador(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String emailEducador = jwtUtil.extraerEmail(token);
        List<PlanTrabajoRespuestaDto> planes = planTrabajoService.obtenerPlanesPorEducador(emailEducador);
        return ResponseEntity.ok(planes);
    }
}
