package com.example.educapp_proyecto.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlanTrabajoRespuestaDto {
    private Long id;
    private String observaciones;
    private String nombreCliente;
    private String emailCliente;
    private List<String> problemas;
    private List<String> actividades;
    private List<String> nombresPerros;
}
