package com.example.educapp_proyecto.service;

import com.example.educapp_proyecto.dto.ClienteRequestDto;
import com.example.educapp_proyecto.dto.ClienteResponseDto;
import com.example.educapp_proyecto.model.Cliente;

import java.util.List;

public interface ClienteServiceInterface {
    List<Cliente> findAll();
    Cliente findById(Long id);
    Cliente save(Cliente entity);
    void deleteById(Long id);
    ClienteResponseDto crearClienteDesdeDto(ClienteRequestDto dto, String emailEducador);
}
