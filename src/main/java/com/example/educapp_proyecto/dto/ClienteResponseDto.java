package com.example.educapp_proyecto.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteResponseDto {
    private Long idCliente;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String nombreEducador;
}
