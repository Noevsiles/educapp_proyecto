package com.example.educapp_proyecto.service;


import com.example.educapp_proyecto.dto.PerroDetalleDto;
import com.example.educapp_proyecto.dto.PerroRequestDto;
import com.example.educapp_proyecto.dto.PerroResponseDto;
import com.example.educapp_proyecto.model.Perro;

import java.util.List;

public interface PerroServiceInterface {
    List<Perro> findAll();
    Perro findById(Long id);
    Perro save(Perro entity);
    void deleteById(Long id);
    Perro updatePerro(Long id, Perro perro);
    PerroResponseDto crearPerro(PerroRequestDto dto);
    PerroResponseDto convertirAPerroDto(Perro perro);
    List<PerroResponseDto> obtenerPerrosPorEducador(String emailEducador);
    void asignarProblemasA(Long idPerro, List<Long> idProblemas);
    PerroDetalleDto convertirADetalleDto(Perro perro);
    List<PerroResponseDto> obtenerPerrosPorCliente(String emailCliente);
}
