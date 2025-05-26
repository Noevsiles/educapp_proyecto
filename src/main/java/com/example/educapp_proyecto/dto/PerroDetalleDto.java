package com.example.educapp_proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerroDetalleDto {
    private Long idPerro;
    private String nombre;
    private String raza;
    private Integer edad;
    private String sexo;
    private Boolean esterilizado;
    private String imagenUrl;
    private List<ProblemaConductaDto> problemasDeConducta;
}
