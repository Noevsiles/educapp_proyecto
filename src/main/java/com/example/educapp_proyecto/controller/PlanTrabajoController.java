package com.example.educapp_proyecto.controller;


import com.example.educapp_proyecto.dto.PlanTrabajoDto;
import com.example.educapp_proyecto.dto.PlanTrabajoRespuestaDto;
import com.example.educapp_proyecto.model.PlanTrabajo;
import com.example.educapp_proyecto.service.PlanTrabajoServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planes")
public class PlanTrabajoController {

    @Autowired
    private PlanTrabajoServiceInterface planService;

    @PostMapping
    public ResponseEntity<PlanTrabajo> crear(@RequestBody PlanTrabajoDto dto) {
        return ResponseEntity.ok(planService.crearPlan(dto));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PlanTrabajoRespuestaDto>> porCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(planService.listarPlanesPorCliente(clienteId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanTrabajo> porId(@PathVariable Long id) {
        return ResponseEntity.ok(planService.buscarPorId(id));
    }
}
