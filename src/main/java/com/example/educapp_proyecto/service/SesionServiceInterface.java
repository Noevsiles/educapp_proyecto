package com.example.educapp_proyecto.service;


import com.example.educapp_proyecto.dto.HuecoAgendaDto;
import com.example.educapp_proyecto.dto.SesionRequestDto;
import com.example.educapp_proyecto.model.Sesion;

import java.time.LocalDate;
import java.util.List;

public interface SesionServiceInterface {

    List<Sesion> findAll();

    Sesion findById(Long id);

    Sesion save(Sesion sesion);

    void deleteById(Long id);

    Sesion crearSesion(SesionRequestDto dto);

    List<HuecoAgendaDto> obtenerHuecosDisponibles(Long idEducador, LocalDate fecha);

}
