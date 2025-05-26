package com.example.educapp_proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolucionDto {
    private Long idSolucion;
    private String nombre;
    private String descripcion;
    private String dificultad;
}
