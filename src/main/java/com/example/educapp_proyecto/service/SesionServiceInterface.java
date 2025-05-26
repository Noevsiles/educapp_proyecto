package com.example.educapp_proyecto.service;


import com.example.educapp_proyecto.dto.*;
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

    Sesion reservarSesion(ReservaSesionDto reserva, String emailEducador);

    List<HuecoAgendaCompletoDto> obtenerAgendaCompleta(Long idEducador, LocalDate fecha);

    Sesion marcarComoRealizada(Long idSesion);

    List<Sesion> filtrarSesiones(Long clienteId, Long perroId, Long educadorId);

    void enviarRecordatorios();

    List<SesionResponseDto> obtenerSesionesPorEducador(String emailEducador);

    void aceptarSesion(Long id);



}
