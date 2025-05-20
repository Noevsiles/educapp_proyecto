package com.example.educapp_proyecto.integration;

import com.example.educapp_proyecto.config.SecurityDisabledConfig;
import com.example.educapp_proyecto.dto.ReservaSesionDto;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.security.test.context.support.WithMockUser;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(SecurityDisabledConfig.class)
public class SesionControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EducadorRepository educadorRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PerroRepository perroRepository;

    @Autowired
    private DisponibilidadRepository disponibilidadRepository;

    @Autowired
    private PlanTrabajoRepository planTrabajoRepository;

    @Autowired
    private ProblemaDeConductaRepository problemaRepository;

    @Autowired
    private ActividadRepository actividadRepository;


    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Transactional
    void testReservarSesionSinSolapamiento() throws Exception {
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

        // Crear disponibilidad del educador
        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setDiaSemana(DayOfWeek.WEDNESDAY);
        disponibilidad.setHoraInicio(LocalTime.of(10, 0));
        disponibilidad.setHoraFin(LocalTime.of(11, 0));
        disponibilidad.setEducador(educador);
        disponibilidadRepository.save(disponibilidad);

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

        // Crear DTO para reservar sesión
        ReservaSesionDto dto = new ReservaSesionDto();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setIdPerro(perro.getIdPerro());
        dto.setIdEducador(educador.getIdEducador());
        dto.setIdPlanTrabajo(plan.getId()); // 👈 Importante: ahora el ID no es null
        dto.setFechaHora(LocalDateTime.of(2025, 5, 21, 10, 0));

        // Realizar petición
        mockMvc.perform(post("/api/sesiones/reservar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    // Test que lanza una excepcion por solapamiento de sesiones
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Transactional
    void testReservarSesionConSolapamiento() throws Exception {
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

        // Crear disponibilidad del educador
        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setDiaSemana(DayOfWeek.WEDNESDAY);
        disponibilidad.setHoraInicio(LocalTime.of(10, 0));
        disponibilidad.setHoraFin(LocalTime.of(11, 0));
        disponibilidad.setEducador(educador);
        disponibilidadRepository.save(disponibilidad);

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

        // Reservar sesión inicial
        ReservaSesionDto dto1 = new ReservaSesionDto();
        dto1.setIdCliente(cliente.getIdCliente());
        dto1.setIdPerro(perro.getIdPerro());
        dto1.setIdEducador(educador.getIdEducador());
        dto1.setIdPlanTrabajo(plan.getId());
        dto1.setFechaHora(LocalDateTime.of(2025, 5, 21, 10, 0)); // Miércoles

        mockMvc.perform(post("/api/sesiones/reservar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto1))
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());

        // Intentar reservar otra a la misma hora con mismo educador → solapamiento
        ReservaSesionDto dto2 = new ReservaSesionDto();
        dto2.setIdCliente(cliente.getIdCliente());
        dto2.setIdPerro(perro.getIdPerro());
        dto2.setIdEducador(educador.getIdEducador());
        dto2.setIdPlanTrabajo(plan.getId());
        dto2.setFechaHora(LocalDateTime.of(2025, 5, 21, 10, 0)); // MISMA hora

        mockMvc.perform(post("/api/sesiones/reservar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2))
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assertTrue(response.contains("Ese horario ya está reservado."));
                });
    }
}
