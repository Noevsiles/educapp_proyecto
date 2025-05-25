package com.example.educapp_proyecto.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerroResponseDto {
    private Long idPerro;
    private String nombre;
    private String raza;
    private String sexo;
    private int edad;
    private boolean esterilizado;
    private String nombreCliente;
    private String imagenUrl;

}
