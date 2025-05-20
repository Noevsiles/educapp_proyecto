package com.example.educapp_proyecto.service;

import com.example.educapp_proyecto.dto.ClienteRequestDto;
import com.example.educapp_proyecto.dto.ClienteResponseDto;
import com.example.educapp_proyecto.model.Cliente;
import com.example.educapp_proyecto.model.Educador;
import com.example.educapp_proyecto.repository.ClienteRepository;
import com.example.educapp_proyecto.repository.EducadorRepository;
import com.example.educapp_proyecto.service.impl.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EducadorRepository educadorRepository;

    @InjectMocks
    private ClienteService clienteService;

    // Test para el metodo de crear cliente desde dto
    @Test
    void testCrearClienteDesdeDto() {
        // DTO de entrada
        ClienteRequestDto dto = new ClienteRequestDto();
        dto.setNombre("Lucía");
        dto.setApellidos("Sánchez");
        dto.setEmail("lucia@example.com");
        dto.setTelefono("123456789");
        dto.setEducadorId(1L);

        // Educador simulado
        Educador educador = new Educador();
        educador.setIdEducador(1L);
        educador.setNombre("Laura");

        // Cliente simulado que devolvería el repositorio tras guardar
        Cliente clienteGuardado = new Cliente();
        clienteGuardado.setIdCliente(10L);
        clienteGuardado.setNombre("Lucía");
        clienteGuardado.setApellidos("Sánchez");
        clienteGuardado.setEmail("lucia@example.com");
        clienteGuardado.setTelefono("123456789");
        clienteGuardado.setEducador(educador);

        // Mocks
        when(educadorRepository.findById(1L)).thenReturn(Optional.of(educador));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);

        // Llamada al metodo
        ClienteResponseDto response = clienteService.crearClienteDesdeDto(dto);

        // Verificaciones
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Lucía", response.getNombre());
        assertEquals("Sánchez", response.getApellidos());
        assertEquals("lucia@example.com", response.getEmail());
        assertEquals("123456789", response.getTelefono());
        assertEquals("Laura", response.getNombreEducador());

        verify(educadorRepository).findById(1L);
        verify(clienteRepository).save(any(Cliente.class));
    }

    // Test para el caso de que el educador no exista
    @Test
    void testCrearClienteDesdeDto_EducadorNoExiste() {
        // DTO con ID de educador inexistente
        ClienteRequestDto dto = new ClienteRequestDto();
        dto.setNombre("Lucía");
        dto.setApellidos("Sánchez");
        dto.setEmail("lucia@example.com");
        dto.setTelefono("123456789");
        dto.setEducadorId(99L); // ID que no existe

        // Simular que el educador no existe
        when(educadorRepository.findById(99L)).thenReturn(Optional.empty());

        // Verificar que se lanza la excepción correcta
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            clienteService.crearClienteDesdeDto(dto);
        });

        // Comprobar el mensaje de error
        assertThrows(RuntimeException.class, () -> clienteService.crearClienteDesdeDto(dto));
        org.junit.jupiter.api.Assertions.assertEquals("Educador no encontrado con id: 99", ex.getMessage());
    }
}
