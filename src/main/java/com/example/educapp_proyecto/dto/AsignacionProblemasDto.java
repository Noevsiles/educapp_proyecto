package com.example.educapp_proyecto.dto;

import lombok.Data;

import java.util.List;

@Data
public class AsignacionProblemasDto {
    private Long idPerro;
    private List<Long> idProblemas;

}
