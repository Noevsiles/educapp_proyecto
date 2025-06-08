package com.example.educapp_proyecto.controller;

import com.example.educapp_proyecto.dto.HuecoAgendaCompletoDto;
import com.example.educapp_proyecto.dto.HuecoAgendaDto;
import com.example.educapp_proyecto.model.DiaSemana;
import com.example.educapp_proyecto.service.SesionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Noelia Vázquez Siles
 * Controlador REST para gestionar la agenda de un educador,
 * incluyendo la consulta de huecos disponibles y la agenda completa.
 */
@RestController
@RequestMapping("/api/educadores/{idEducador}/agenda")
public class AgendaController {

    @Autowired
    private SesionServiceInterface sesionService;

    /**
     * Obtiene los huecos disponibles en la agenda de un educador para una fecha concreta.
     *
     * @param educadorId ID del educador.
     * @param fecha Fecha en formato yyyy-MM-dd para consultar la disponibilidad.
     * @return Lista de cadenas con las horas disponibles en formato "HH:mm".
     */
    // Obtener huecos disponibles de la agenda
    @GetMapping("/disponibles")
    public ResponseEntity<List<String>> obtenerDisponibilidad(
            @RequestParam Long educadorId,
            @RequestParam String fecha
    ) {
        LocalDate fechaLocal = LocalDate.parse(fecha);

        List<String> disponibles = sesionService.obtenerHuecosDisponibles(educadorId, fechaLocal);
        return ResponseEntity.ok(disponibles);
    }

    /**
     * Obtiene la agenda completa del educador para una fecha específica, incluyendo huecos ocupados y libres.
     *
     * @param idEducador ID del educador.
     * @param fecha Fecha para la que se desea consultar la agenda.
     * @return Lista de objetos {@link HuecoAgendaCompletoDto} representando el estado de cada hueco horario.
     */
    // Obtener la agenda completa del adiestrador
    @GetMapping("/completa")
    public List<HuecoAgendaCompletoDto> obtenerAgendaCompleta(
            @PathVariable("idEducador") Long idEducador,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return sesionService.obtenerAgendaCompleta(idEducador, fecha);
    }
}
