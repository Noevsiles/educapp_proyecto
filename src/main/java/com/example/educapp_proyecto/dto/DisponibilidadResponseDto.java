package com.example.educapp_proyecto.dto;


import com.example.educapp_proyecto.model.DiaSemana;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisponibilidadResponseDto {
    private Long id;
    private DiaSemana diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
