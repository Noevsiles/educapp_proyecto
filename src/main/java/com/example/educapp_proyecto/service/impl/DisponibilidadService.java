package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.dto.DisponibilidadRequestDto;
import com.example.educapp_proyecto.dto.DisponibilidadResponseDto;
import com.example.educapp_proyecto.model.DiaSemana;
import com.example.educapp_proyecto.model.Disponibilidad;
import com.example.educapp_proyecto.model.Educador;
import com.example.educapp_proyecto.repository.DisponibilidadRepository;
import com.example.educapp_proyecto.repository.EducadorRepository;
import com.example.educapp_proyecto.service.DisponibilidadServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona la disponibilidad semanal de los educadores.
 * Permite crear, actualizar, eliminar y obtener disponibilidades asociadas a un educador.
 *
 * @author Noelia Vázquez Siles
 */
@Service
public class DisponibilidadService implements DisponibilidadServiceInterface {

    @Autowired
    private DisponibilidadRepository disponibilidadRepository;

    @Autowired
    private EducadorRepository educadorRepository;

    // Guardar disponibilidad
    /**
     * Guarda una nueva disponibilidad para el educador autenticado.
     *
     * @param dto datos de disponibilidad a guardar.
     * @param email email del educador autenticado.
     */
    @Override
    public void guardarDisponibilidad(DisponibilidadRequestDto dto, String email) {
        Educador educador = educadorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Educador no encontrado"));

        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setEducador(educador);
        disponibilidad.setDiaSemana(dto.getDiaSemana());
        disponibilidad.setHoraInicio(dto.getHoraInicio());
        disponibilidad.setHoraFin(dto.getHoraFin());

        disponibilidadRepository.save(disponibilidad);
    }

    // Actualizar disponibilidad
    /**
     * Actualiza una disponibilidad existente por su ID.
     *
     * @param id ID de la disponibilidad a actualizar.
     * @param dto nuevos datos de disponibilidad.
     * @param email email del educador autenticado.
     */
    @Override
    public void actualizar(Long id, DisponibilidadRequestDto dto, String email) {
        Disponibilidad disponibilidad = disponibilidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));

        Educador educador = educadorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Educador no encontrado"));

        disponibilidad.setEducador(educador);
        disponibilidad.setDiaSemana(DiaSemana.valueOf(dto.getDiaSemana().toString()));
        disponibilidad.setHoraInicio(dto.getHoraInicio());
        disponibilidad.setHoraFin(dto.getHoraFin());

        disponibilidadRepository.save(disponibilidad);
    }

    // Eliminar una disponibilidad por su id
    /**
     * Elimina una disponibilidad por su ID.
     *
     * @param id ID de la disponibilidad a eliminar.
     */
    @Override
    public void eliminarPorId(Long id) {
        disponibilidadRepository.deleteById(id);
    }

    // Obtener el educador por su email
    /**
     * Obtiene todas las disponibilidades de un educador a partir de su ID.
     *
     * @param educadorId ID del educador.
     * @return lista de disponibilidades como DTOs.
     */
    @Override
    public List<DisponibilidadResponseDto> obtenerPorEducador(Long educadorId) {
        List<Disponibilidad> disponibilidad = disponibilidadRepository.findByEducador_IdEducador(educadorId);

        return disponibilidad.stream()
                .map(d -> new DisponibilidadResponseDto(
                        d.getId(),
                        d.getDiaSemana(),
                        d.getHoraInicio(),
                        d.getHoraFin()))
                .collect(Collectors.toList());
    }

    // Obtener al educador a traves de su email (para autenticacion por token)
    /**
     * Obtiene todas las disponibilidades del educador autenticado por su email.
     *
     * @param email email del educador.
     * @return lista de disponibilidades como DTOs.
     */
    @Override
    public List<DisponibilidadResponseDto> obtenerPorEmailEducador(String email) {
        Educador educador = educadorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Educador no encontrado"));

        List<Disponibilidad> disponibilidad = disponibilidadRepository.findByEducador_IdEducador(educador.getIdEducador());

        return disponibilidad.stream()
                .map(d -> new DisponibilidadResponseDto(
                        d.getId(),
                        d.getDiaSemana(),
                        d.getHoraInicio(),
                        d.getHoraFin()))
                .collect(Collectors.toList());
    }

}

