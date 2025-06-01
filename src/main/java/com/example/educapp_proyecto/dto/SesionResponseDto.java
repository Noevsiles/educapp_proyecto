package com.example.educapp_proyecto.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SesionResponseDto {
    private Long id;
    private String nombrePerro;
    private String actividad;
    private LocalDateTime fechaHora;
    private boolean realizada;
    private boolean aceptada;
    private boolean rechazada;
}

