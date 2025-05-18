package com.example.educapp_proyecto.controller;

import com.example.educapp_proyecto.dto.HuecoAgendaDto;
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
    public ResponseEntity<List<HuecoAgendaDto>> obtenerHuecos(
            @PathVariable Long idEducador,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        List<HuecoAgendaDto> slots = sesionService.obtenerHuecosDisponibles(idEducador, fecha);
        return ResponseEntity.ok(slots);
    }
}
