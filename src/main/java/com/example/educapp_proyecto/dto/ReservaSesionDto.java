package com.example.educapp_proyecto.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservaSesionDto {

    private Long idCliente;
    private Long idPerro;
    private Long idPlanTrabajo;
    private Long idEducador;
    private LocalDateTime fechaHora;


}
