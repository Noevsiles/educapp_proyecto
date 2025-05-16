package com.example.educapp_proyecto.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SesionRequestDto {
    private LocalDateTime fechaHora;
    private String tipoSesion;
    private String observaciones;
    private Long clienteId;
    private Long educadorId;
    private Long perroId;
    private Long planTrabajoId;
}
