package com.example.educapp_proyecto.integration;

import com.example.educapp_proyecto.config.SecurityDisabledConfig;
import com.example.educapp_proyecto.model.Educador;
import com.example.educapp_proyecto.repository.EducadorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(SecurityDisabledConfig.class)
public class ClienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EducadorRepository educadorRepository;

    private Long idEducador;

    @BeforeEach
    void setUp() {
        Educador educador = new Educador();
        educador.setNombre("EduControlador");
        educador.setApellidos("Test");
        educador.setEmail("edu@test.com");
        educador.setTelefono("600000000");
        educador.setEspecializacion("Ansiedad");
        educador.setExperiencia(3);
        educador.setFormacion("Psicología canina");
        educador.setDescripcion("Test educador");

        idEducador = educadorRepository.save(educador).getIdEducador();
    }

    // Test para crear cliente desde el endpoint /api/clientes
    @Test
    void testCrearClienteEndpoint() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("nombre", "Sara");
        request.put("apellidos", "Domínguez");
        request.put("email", "sara@correo.com");
        request.put("telefono", "654321987");
        request.put("educadorId", idEducador);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Sara"))
                .andExpect(jsonPath("$.nombreEducador").value("EduControlador"));
    }
}