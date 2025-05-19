package com.example.educapp_proyecto.service;


import com.example.educapp_proyecto.dto.PlanTrabajoDto;
import com.example.educapp_proyecto.dto.PlanTrabajoRespuestaDto;
import com.example.educapp_proyecto.model.*;
import com.example.educapp_proyecto.repository.ActividadRepository;
import com.example.educapp_proyecto.repository.ClienteRepository;
import com.example.educapp_proyecto.repository.PlanTrabajoRepository;
import com.example.educapp_proyecto.repository.ProblemaDeConductaRepository;
import com.example.educapp_proyecto.service.impl.PlanTrabajoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PlanTrabajoServiceTest {

    @Mock
    private PlanTrabajoRepository planTrabajoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProblemaDeConductaRepository problemaDeConductaRepository;

    @Mock
    private ActividadRepository actividadRepository;

    @InjectMocks
    private PlanTrabajoService planTrabajoService;

    // Test para ver si funciona el metodo save()
    @Test
    void testGuardarPlanTrabajo() {
        // Dado
        PlanTrabajo plan = new PlanTrabajo();
        plan.setObservaciones("Plan para reforzar obediencia");

        // Cuando se llama al save del repositorio, devolvemos el mismo plan
        when(planTrabajoRepository.save(plan)).thenReturn(plan);

        // Cuando
        PlanTrabajo resultado = planTrabajoService.save(plan);

        // Entonces
        assertNotNull(resultado);
        assertEquals("Plan para reforzar obediencia", resultado.getObservaciones());
        verify(planTrabajoRepository).save(plan); // esto verifica que se llamó al repositorio
    }

    // Test para respuesta satisfactoria al crear un nuevo plan de trabajo
    @Test
    void testCrearPlanTrabajo() {
        // DTO de entrada simulado
        PlanTrabajoDto dto = new PlanTrabajoDto();
        dto.setIdCliente(1L);
        dto.setProblemaIds(List.of(10L, 20L));
        dto.setActividadIds(List.of(100L, 200L));

        // Objetos simulados para que devuelvan los mocks
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setNombre("Cliente Test");

        ProblemaDeConducta problema1 = new ProblemaDeConducta();
        problema1.setIdProblema(10L);

        ProblemaDeConducta problema2 = new ProblemaDeConducta();
        problema2.setIdProblema(20L);

        Actividad actividad1 = new Actividad();
        actividad1.setIdActividad(100L);

        Actividad actividad2 = new Actividad();
        actividad2.setIdActividad(200L);

        PlanTrabajo planEsperado = new PlanTrabajo();
        planEsperado.setId(1L);
        planEsperado.setCliente(cliente);

        // Configurar comportamiento de los mocks
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(problemaDeConductaRepository.findAllById(List.of(10L, 20L))).thenReturn(List.of(problema1, problema2));
        when(actividadRepository.findAllById(List.of(100L, 200L))).thenReturn(List.of(actividad1, actividad2));
        when(planTrabajoRepository.save(any(PlanTrabajo.class))).thenReturn(planEsperado);

        // Ejecutar el metodo del servicio
        PlanTrabajo resultado = planTrabajoService.crearPlan(dto);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Cliente Test", resultado.getCliente().getNombre());

        verify(clienteRepository).findById(1L);
        verify(problemaDeConductaRepository).findAllById(List.of(10L, 20L));
        verify(actividadRepository).findAllById(List.of(100L, 200L));
        verify(planTrabajoRepository).save(any(PlanTrabajo.class));
    }

    // Test para verificar que si se llama a crearPlan() con un id de cliente que no existe, el servicio lanza una excepcion
    @Test
    void testCrearPlanTrabajo_clienteNoEncontrado_lanzaExcepcion() {
        // DTO de entrada con ID de cliente inexistente
        PlanTrabajoDto dto = new PlanTrabajoDto();
        dto.setIdCliente(999L); // ID que no existe
        dto.setProblemaIds(List.of(1L));
        dto.setActividadIds(List.of(1L));

        // Configurar el mock para que el cliente no exista
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Ejecutar y verificar que lanza RuntimeException
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            planTrabajoService.crearPlan(dto);
        });

        assertEquals("Cliente no encontrado", ex.getMessage());

        // Verificamos que solo se haya intentado buscar al cliente
        verify(clienteRepository).findById(999L);
        verifyNoMoreInteractions(problemaDeConductaRepository, actividadRepository, planTrabajoRepository);
    }

    // Test para verificar si funciona el metodo para obtener todos los planes de trabajo
    @Test
    void testObtenerTodos() {
        // Crear lista simulada de planes
        PlanTrabajo plan1 = new PlanTrabajo();
        plan1.setId(1L);
        plan1.setObservaciones("Plan 1");

        PlanTrabajo plan2 = new PlanTrabajo();
        plan2.setId(2L);
        plan2.setObservaciones("Plan 2");

        List<PlanTrabajo> listaSimulada = List.of(plan1, plan2);

        // Simular comportamiento del repositorio
        when(planTrabajoRepository.findAll()).thenReturn(listaSimulada);

        // Llamar al metodo del servicio
        List<PlanTrabajo> resultado = planTrabajoService.obtenerTodos();

        // Verificar resultados
        assertEquals(2, resultado.size());
        assertEquals("Plan 1", resultado.get(0).getObservaciones());
        assertEquals("Plan 2", resultado.get(1).getObservaciones());

        // Verificar llamada al repositorio
        verify(planTrabajoRepository).findAll();
    }

    // Test para verificar que el metodo buscarPorId() funciona
    @Test
    void testBuscarPorId_Existe() {
        PlanTrabajo plan = new PlanTrabajo();
        plan.setId(1L);
        plan.setObservaciones("Buscar plan existente");

        when(planTrabajoRepository.findById(1L)).thenReturn(Optional.of(plan));

        PlanTrabajo resultado = planTrabajoService.buscarPorId(1L);

        assertEquals(plan, resultado);
        verify(planTrabajoRepository).findById(1L);
    }

    // Test para verificar que el metodo buscarPorId() lanza una excepcion si no encuentra el id
    @Test
    void testBuscarPorId_NoExiste() {
        when(planTrabajoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            planTrabajoService.buscarPorId(99L);
        });

        verify(planTrabajoRepository).findById(99L);
    }

    // Test para probar si funciona el metodo eliminar
    @Test
    void testEliminarPorId() {
        Long id = 1L;

        // No hace falta simular nada si no lanza excepción
        doNothing().when(planTrabajoRepository).deleteById(id);

        // Llamar al metodo
        planTrabajoService.eliminarPorId(id);

        // Verificar que se llamo a deleteById
        verify(planTrabajoRepository).deleteById(id);
    }
        // Test para comprobar que el metodo listarPlanesPorCliente() funciona
        @Test
        void testListarPlanesPorCliente() {
            Long clienteId = 1L;

            // Crear datos simulados
            Cliente cliente = new Cliente();
            cliente.setIdCliente(clienteId);
            cliente.setNombre("Cliente de prueba");
            cliente.setEmail("cliente@prueba.com");

            Perro perro = new Perro();
            perro.setNombre("Firulais");
            perro.setCliente(cliente);

            cliente.setPerros(List.of(perro));

            ProblemaDeConducta problema = new ProblemaDeConducta();
            problema.setNombre("Ladra mucho");

            Actividad actividad = new Actividad();
            actividad.setNombre("Paseos diarios");

            PlanTrabajo plan = new PlanTrabajo();
            plan.setId(10L);
            plan.setCliente(cliente);
            plan.setObservaciones("Observaciones test");
            plan.setProblemas(Set.of(problema));
            plan.setActividades(Set.of(actividad));

            // Simular respuesta del repositorio
            when(planTrabajoRepository.findByClienteIdWithRelations(clienteId)).thenReturn(List.of(plan));

            // Ejecutar
            List<PlanTrabajoRespuestaDto> resultado = planTrabajoService.listarPlanesPorCliente(clienteId);

            // Verificar
            assertEquals(1, resultado.size());
            PlanTrabajoRespuestaDto dto = resultado.get(0);
            assertEquals("Cliente de prueba", dto.getNombreCliente());
            assertEquals("cliente@prueba.com", dto.getEmailCliente());
            assertEquals(List.of("Ladra mucho"), dto.getProblemas());
            assertEquals(List.of("Paseos diarios"), dto.getActividades());
            assertEquals(List.of("Firulais"), dto.getNombresPerros());

            verify(planTrabajoRepository).findByClienteIdWithRelations(clienteId);
        }
}
