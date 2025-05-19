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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
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
}
