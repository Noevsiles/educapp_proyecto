package com.example.educapp_proyecto.service;

import com.example.educapp_proyecto.dto.PlanTrabajoDto;
import com.example.educapp_proyecto.dto.PlanTrabajoRespuestaDto;
import com.example.educapp_proyecto.model.PlanTrabajo;

import java.util.List;

public interface PlanTrabajoServiceInterface  {

    PlanTrabajo crearPlan(PlanTrabajoDto dto);
    List<PlanTrabajoRespuestaDto> listarPlanesPorCliente(Long idCliente);
    PlanTrabajo buscarPorId(Long id);
}
