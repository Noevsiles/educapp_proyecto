package com.example.educapp_proyecto.service;

import com.example.educapp_proyecto.dto.PerroRequestDto;
import com.example.educapp_proyecto.dto.PerroResponseDto;
import com.example.educapp_proyecto.model.Cliente;
import com.example.educapp_proyecto.model.Perro;
import com.example.educapp_proyecto.repository.ClienteRepository;
import com.example.educapp_proyecto.repository.PerroRepository;
import com.example.educapp_proyecto.service.impl.PerroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PerroServiceTest {
    @Mock
    private PerroRepository perroRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private PerroService perroService;

    private Cliente cliente;
    private Perro perro;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setNombre("Pedro");

        perro = new Perro();
        perro.setIdPerro(10L);
        perro.setNombre("Max");
        perro.setRaza("Labrador");
        perro.setSexo("Macho");
        perro.setEdad(3);
        perro.setEsterilizado(true);
        perro.setCliente(cliente);
    }

    // Test para crear perro
    @Test
    void testCrearPerro() {
        // DTO de entrada
        PerroRequestDto dto = new PerroRequestDto();
        dto.setClienteId(1L);
        dto.setNombre("Max");
        dto.setRaza("Labrador");
        dto.setSexo("Macho");
        dto.setEdad(3);
        dto.setEsterilizado(true);

        // Simulaciones
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(perroRepository.save(any(Perro.class))).thenReturn(perro);

        // Ejecución
        PerroResponseDto response = perroService.crearPerro(dto);

        // Verificaciones
        assertNotNull(response);
        assertEquals("Max", response.getNombre());
        assertEquals("Labrador", response.getRaza());
        assertEquals("Pedro", response.getNombreCliente());
        verify(clienteRepository).findById(1L);
        verify(perroRepository).save(any(Perro.class));
    }

    // Test para qrear perro con cliente que no existe y lance excepcion
    @Test
    void testCrearPerro_ClienteNoExiste_lanzaExcepcion() {
        // DTO con un cliente que no existe
        PerroRequestDto dto = new PerroRequestDto();
        dto.setClienteId(999L);
        dto.setNombre("Max");
        dto.setRaza("Labrador");
        dto.setSexo("Macho");
        dto.setEdad(3);
        dto.setEsterilizado(true);

        // Simular que no se encuentra el cliente
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Verificar que lanza la excepción esperada
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            perroService.crearPerro(dto);
        });

        assertEquals("Cliente no encontrado con ID 999", ex.getMessage());
        verify(clienteRepository).findById(999L);
        verifyNoInteractions(perroRepository);
    }

}
