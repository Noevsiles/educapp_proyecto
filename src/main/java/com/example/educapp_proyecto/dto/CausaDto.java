package com.example.educapp_proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CausaDto {
    private Long idCausa;
    private String descripcion;
}
