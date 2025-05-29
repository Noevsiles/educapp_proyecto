package com.example.educapp_proyecto.service;

import com.example.educapp_proyecto.dto.PlanTrabajoClienteDto;
import com.example.educapp_proyecto.dto.PlanTrabajoDto;
import com.example.educapp_proyecto.dto.PlanTrabajoRespuestaDto;
import com.example.educapp_proyecto.model.PlanTrabajo;

import java.util.List;

public interface PlanTrabajoServiceInterface  {

    PlanTrabajo crearPlan(PlanTrabajoDto dto);
    List<PlanTrabajoRespuestaDto> listarPlanesPorCliente(Long idCliente);
    PlanTrabajo buscarPorId(Long id);

    // Eliminar un plan de trabajo por id
    void eliminarPorId(Long id);

    List<PlanTrabajoClienteDto> obtenerPlanesPorCliente(String emailCliente);

    List<PlanTrabajoRespuestaDto> obtenerPlanesPorEducador(String emailEducador);
}
