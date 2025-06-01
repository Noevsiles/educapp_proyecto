package com.example.educapp_proyecto.controller;

import com.example.educapp_proyecto.dto.RegistroClienteDto;
import com.example.educapp_proyecto.service.impl.RegistroClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** @author Noelia Vázquez Siles
 * Controlador REST para el registro de nuevos clientes en la plataforma.
 */
@RestController
@RequestMapping("/api/registro")
public class RegistroController {

    @Autowired
    private RegistroClienteService registroClienteService;

    /**
     * Registra un nuevo cliente en el sistema.
     *
     * @param dto DTO con los datos del cliente a registrar.
     * @return Respuesta HTTP 200 si el registro se realiza correctamente.
     */
    @PostMapping("/cliente")
    public ResponseEntity<?> registrarCliente(@RequestBody RegistroClienteDto dto) {
        registroClienteService.registrarCliente(dto);
        return ResponseEntity.ok().build();
    }
}
