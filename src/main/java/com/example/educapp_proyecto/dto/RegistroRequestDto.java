package com.example.educapp_proyecto.dto;

import lombok.Data;

@Data
public class RegistroRequestDto {
    private String email;
    private String nombre;
    private String password;
    private String rol;
}

