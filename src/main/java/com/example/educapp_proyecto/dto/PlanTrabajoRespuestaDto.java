package com.example.educapp_proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanTrabajoRespuestaDto {
    private Long id;
    private String observaciones;
    private Integer numeroSesiones;
    private String nombreCliente;
    private String emailCliente;
    private List<String> problemas;
    private List<String> actividades;
    private List<String> nombresPerros;
}
