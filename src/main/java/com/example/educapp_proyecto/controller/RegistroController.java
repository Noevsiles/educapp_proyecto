package com.example.educapp_proyecto.controller;

import com.example.educapp_proyecto.dto.RegistroClienteDto;
import com.example.educapp_proyecto.service.impl.RegistroClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registro")
public class RegistroController {

    @Autowired
    private RegistroClienteService registroClienteService;

    @PostMapping("/cliente")
    public ResponseEntity<?> registrarCliente(@RequestBody RegistroClienteDto dto) {
        registroClienteService.registrarCliente(dto);
        return ResponseEntity.ok().build();
    }
}
