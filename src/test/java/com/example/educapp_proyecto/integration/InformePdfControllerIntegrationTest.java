package com.example.educapp_proyecto.integration;

import com.example.educapp_proyecto.config.SecurityDisabledConfig;
import com.example.educapp_proyecto.model.*;
import com.example.educapp_proyecto.repository.*;
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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(SecurityDisabledConfig.class)
public class InformePdfControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EducadorRepository educadorRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PerroRepository perroRepository;

    @Autowired
    private ProblemaDeConductaRepository problemaRepository;

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private PlanTrabajoRepository planTrabajoRepository;

    // Test para probar si se genera el informe pdf
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Transactional
    void testGenerarInformePdfCliente() throws Exception {
        // Crear educador
        Educador educador = new Educador();
        educador.setNombre("Pepe");
        educador.setApellidos("López");
        educador.setEmail("edu@mail.com");
        educador.setTelefono("600111222");
        educador.setEspecializacion("Obediencia");
        educador.setDescripcion("Educador experto");
        educador.setFormacion("FP canina");
        educador.setExperiencia(4);
        educador = educadorRepository.save(educador);

        // Crear cliente
        Cliente cliente = new Cliente();
        cliente.setNombre("Laura");
        cliente.setApellidos("Martín");
        cliente.setEmail("laura@mail.com");
        cliente.setTelefono("699888777");
        cliente.setEducador(educador);
        cliente = clienteRepository.save(cliente);

        // Crear perro
        Perro perro = new Perro();
        perro.setNombre("Toby");
        perro.setEdad(3);
        perro.setSexo("Macho");
        perro.setRaza("Golden Retriever");
        perro.setEsterilizado(true);
        perro.setCliente(cliente);
        perro = perroRepository.save(perro);

        // Crear problema de conducta
        ProblemaDeConducta problema = new ProblemaDeConducta();
        problema.setNombre("Ladrido excesivo");
        problema.setDescripcion("El perro ladra de forma compulsiva");
        problema = problemaRepository.save(problema);

        // Crear actividad
        Actividad actividad = new Actividad();
        actividad.setNombre("Control de ladridos");
        actividad.setDescripcion("Ejercicio de control mediante comandos y refuerzo positivo");
        actividad = actividadRepository.save(actividad);

        // Crear plan de trabajo
        PlanTrabajo plan = new PlanTrabajo();
        plan.setCliente(cliente);
        plan.setProblemas(Set.of(problema));
        plan.setActividades(Set.of(actividad));
        plan = planTrabajoRepository.save(plan);

        // Ejecutar la petición GET para obtener el PDF
        mockMvc.perform(get("/api/informes/perro/" + perro.getIdPerro() + "/pdf")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/pdf"))
                .andExpect(result ->
                        assertTrue(result.getResponse().getContentAsByteArray().length > 0,
                                "El contenido del PDF no debe estar vacío"));
    }
}
