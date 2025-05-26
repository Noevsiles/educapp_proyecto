package com.example.educapp_proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemaConductaDto {
    private Long idProblemaConducta;
    private String nombre;
    private String descripcion;
    private List<CausaDto> causas;
    private List<SolucionDto> soluciones;


}
