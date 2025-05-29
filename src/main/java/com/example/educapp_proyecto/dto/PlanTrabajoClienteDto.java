package com.example.educapp_proyecto.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanTrabajoClienteDto {
    private Long idPlan;
    private String nombre;
    private String observaciones;
    private PerroMiniDto perro;
    private List<ProblemaMiniDto> problemas;
    private List<ActividadMiniDto> actividades;
}
