package com.example.educapp_proyecto.integration;

import com.example.educapp_proyecto.config.SecurityDisabledConfig;
import com.example.educapp_proyecto.dto.PlanTrabajoDto;
import com.example.educapp_proyecto.model.*;
import com.example.educapp_proyecto.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(SecurityDisabledConfig.class)
public class PlanTrabajoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProblemaDeConductaRepository problemaRepository;

    @Autowired
    private ActividadRepository actividadRepository;

    // Test para crear un plan de trabajo usando la api rest
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Transactional
    void testCrearPlanTrabajo() throws Exception {
        // Crear cliente
        Cliente cliente = new Cliente();
        cliente.setNombre("Ana");
        cliente.setApellidos("García");
        cliente.setEmail("ana@mail.com");
        cliente.setTelefono("123456789");
        cliente = clienteRepository.save(cliente);

        // Crear problema de conducta
        ProblemaDeConducta problema = new ProblemaDeConducta();
        problema.setNombre("Ansiedad");
        problema.setDescripcion("Ansiedad por separación");
        problema = problemaRepository.save(problema);

        // Crear actividad
        Actividad actividad = new Actividad();
        actividad.setNombre("Ejercicio de relajación");
        actividad.setDescripcion("Técnicas de relajación para perros ansiosos");
        actividad = actividadRepository.save(actividad);

        // Crear DTO
        PlanTrabajoDto dto = new PlanTrabajoDto();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setProblemaIds(List.of(problema.getIdProblema()));
        dto.setActividadIds(List.of(actividad.getIdActividad()));
        dto.setObservaciones("Plan específico para ansiedad por separación");
        dto.setNombresPerros(List.of("Max"));

        // Realizar petición
        mockMvc.perform(post("/api/planes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }
}