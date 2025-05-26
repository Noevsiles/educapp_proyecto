package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.dto.PlanTrabajoDto;
import com.example.educapp_proyecto.dto.PlanTrabajoRespuestaDto;
import com.example.educapp_proyecto.model.PlanTrabajo;
import com.example.educapp_proyecto.service.PlanTrabajoServiceInterface;
import com.example.educapp_proyecto.service.impl.PlanTrabajoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planes")
public class PlanTrabajoController {

    @Autowired
    private PlanTrabajoServiceInterface planService;

    @Autowired
    private PlanTrabajoService planTrabajoService;

    @PostMapping
    public ResponseEntity<PlanTrabajo> crear(@RequestBody PlanTrabajoDto dto) {
        PlanTrabajo plan = planService.crearPlan(dto);
        return new ResponseEntity<>(plan, HttpStatus.CREATED);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PlanTrabajoRespuestaDto>> porCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(planService.listarPlanesPorCliente(clienteId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanTrabajo> porId(@PathVariable Long id) {
        return ResponseEntity.ok(planService.buscarPorId(id));
    }


    @GetMapping("/todos")
    public ResponseEntity<List<PlanTrabajo>> obtenerTodos() {
        List<PlanTrabajo> planes = planTrabajoService.obtenerTodos();
        return ResponseEntity.ok(planes);
    }

    @PostMapping("/save")
    public ResponseEntity<PlanTrabajo> save(@RequestBody PlanTrabajo plan) {
        PlanTrabajo creado = planTrabajoService.save(plan);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creado);
    }

    // Eliminar un plan de trabajo por su id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id) {
        planTrabajoService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

}
