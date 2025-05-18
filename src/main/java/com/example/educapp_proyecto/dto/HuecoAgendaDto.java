package com.example.educapp_proyecto.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HuecoAgendaDto {

    private LocalDateTime inicio;
    private LocalDateTime fin;
}
