package com.example.educapp_proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActividadDto {
    private Long idActividad;
    private String nombre;
    private String descripcion;
    private int duracion;
    private boolean completado;
}