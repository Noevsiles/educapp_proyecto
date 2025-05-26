package com.example.educapp_proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolucionAplicadaRequest {
    private Long idProblemaConducta;
    private Long idSolucion;
    private Integer numeroDeSesiones;
    private List<ActividadDto> actividades;
}