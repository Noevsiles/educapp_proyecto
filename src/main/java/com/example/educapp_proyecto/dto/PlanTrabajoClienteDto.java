package com.example.educapp_proyecto.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlanTrabajoClienteDto {
    private Long idPlan;
    private String nombre;
    private String observaciones;
    private PerroMiniDto perro;
    private List<ProblemaMiniDto> problemas;
    private List<ActividadMiniDto> actividades;
}
