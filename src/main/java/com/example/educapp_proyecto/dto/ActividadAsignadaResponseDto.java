package com.example.educapp_proyecto.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActividadAsignadaResponseDto {
    private Long id;
    private String nombreActividad;
    private String descripcion;
    private boolean completada;
}