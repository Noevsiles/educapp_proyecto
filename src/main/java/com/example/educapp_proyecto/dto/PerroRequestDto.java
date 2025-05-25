package com.example.educapp_proyecto.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerroRequestDto {
    private String nombre;
    private String raza;
    private String sexo;
    private int edad;
    private boolean esterilizado;
    private Long clienteId;
    private String imagenUrl;
}
