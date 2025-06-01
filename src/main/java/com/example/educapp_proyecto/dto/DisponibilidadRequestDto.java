package com.example.educapp_proyecto.dto;

import com.example.educapp_proyecto.model.DiaSemana;
import lombok.Data;

import java.time.LocalTime;

@Data
public class DisponibilidadRequestDto {
    private Long educadorId;
    private DiaSemana diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}

