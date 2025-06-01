package com.example.educapp_proyecto.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SesionClienteResponseDto {
    private Long id;
    private String nombrePerro;
    private String actividad;
    private LocalDateTime fechaHora;
    private Boolean aceptada;
    private Boolean rechazada;
    private Boolean realizada;
}
