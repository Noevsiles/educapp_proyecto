package com.example.educapp_proyecto.integration;
import com.example.educapp_proyecto.dto.ClienteRequestDto;
import com.example.educapp_proyecto.dto.ClienteResponseDto;
import com.example.educapp_proyecto.model.Educador;
import com.example.educapp_proyecto.repository.EducadorRepository;
import com.example.educapp_proyecto.service.impl.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ClienteServiceIntegrationTest {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EducadorRepository educadorRepository;

    private Long idEducador;

    @BeforeEach
    void setUp() {
        // Insertamos un educador en la base de datos para asociarlo
        Educador educador = new Educador();
        educador.setNombre("Laura Educadora");
        idEducador = educadorRepository.save(educador).getIdEducador();
    }

    // Test para crear cliente desde el dto
    @Test
    void testCrearClienteDesdeDto_Integracion() {
        // Guardar educador en la base de datos
        Educador educador = new Educador();
        educador.setNombre("Laura Educadora");
        educador.setEmail("laura@educadora.com");
        educador.setApellidos("Gómez");
        educador.setTelefono("123123123");
        educador.setExperiencia(5);
        educador.setFormacion("Etología canina");
        educador.setEspecializacion("Conducta");
        educador.setDescripcion("Especialista en modificación de conducta");

        educador = educadorRepository.save(educador);

        // Crear el cliente DTO
        ClienteRequestDto dto = new ClienteRequestDto();
        dto.setNombre("Mario");
        dto.setApellidos("Ríos");
        dto.setEmail("mario@example.com");
        dto.setTelefono("666999123");

        // Ejecutar
        ClienteResponseDto response = clienteService.crearClienteDesdeDto(dto, "laura@educadora.com");

        // Verificar
        assertNotNull(response.getId());
        assertEquals("Mario", response.getNombre());
        assertEquals("Ríos", response.getApellidos());
        assertEquals("mario@example.com", response.getEmail());
        assertEquals("666999123", response.getTelefono());
        assertEquals("Laura Educadora", response.getNombreEducador());
    }
}
