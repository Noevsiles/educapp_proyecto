package com.example.educapp_proyecto.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanTrabajoDto {

    private Long idCliente;
    private Long idPerro;
    private List<Long> problemaIds;
    private List<Long> actividadIds;
    private String observaciones;
    private List<String> nombresPerros;
    private List<Long> idActividades;
    private List<SolucionAplicadaRequest> solucionesAplicadas;
    private Integer numeroSesiones;

}
