package com.example.educapp_proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HuecoAgendaCompletoDto {
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private boolean ocupado;
}
