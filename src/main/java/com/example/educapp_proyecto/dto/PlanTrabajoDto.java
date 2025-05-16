package com.example.educapp_proyecto.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlanTrabajoDto {

    private Long idCliente;
    private List<Long> problemaIds;
    private List<Long> actividadIds;
    private String observaciones;
    private List<String> nombresPerros;
}
