package com.example.educapp_proyecto.service;

import com.example.educapp_proyecto.dto.DisponibilidadRequestDto;
import com.example.educapp_proyecto.dto.DisponibilidadResponseDto;

import java.util.List;

public interface DisponibilidadServiceInterface {
    void guardarDisponibilidad(DisponibilidadRequestDto dto, String emailEducador);
    List<DisponibilidadResponseDto> obtenerPorEducador(Long educadorId);

    List<DisponibilidadResponseDto> obtenerPorEmailEducador(String email);

    void eliminarPorId(Long id);

    void actualizar(Long id, DisponibilidadRequestDto dto, String email);
}

