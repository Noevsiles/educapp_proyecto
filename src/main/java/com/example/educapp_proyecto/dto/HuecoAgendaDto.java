package com.example.educapp_proyecto.dto;


import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HuecoAgendaDto {

    private LocalDateTime inicio;
    private LocalDateTime fin;
    private boolean ocupado;
}
