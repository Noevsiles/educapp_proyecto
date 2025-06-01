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

@RestController
@RequestMapping("/api/educadores/{idEducador}/agenda")
public class AgendaController {

    @Autowired
    private SesionServiceInterface sesionService;

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


    // Obtener la agenda completa del adiestrador
    @GetMapping("/completa")
    public List<HuecoAgendaCompletoDto> obtenerAgendaCompleta(
            @PathVariable("idEducador") Long idEducador,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return sesionService.obtenerAgendaCompleta(idEducador, fecha);
    }
}
