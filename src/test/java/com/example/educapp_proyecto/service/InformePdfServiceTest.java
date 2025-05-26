package com.example.educapp_proyecto.service;

import com.example.educapp_proyecto.model.*;
import com.example.educapp_proyecto.repository.PerroRepository;
import com.example.educapp_proyecto.repository.PlanTrabajoRepository;
import com.example.educapp_proyecto.service.impl.InformePdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InformePdfServiceTest {

    @Mock
    private PerroRepository perroRepository;

    @Mock
    private PlanTrabajoRepository planTrabajoRepository;

    @InjectMocks
    private InformePdfService informePdfService;

    private Perro perro;
    private Cliente cliente;
    private PlanTrabajo planTrabajo;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setNombre("Ana");
        cliente.setApellidos("García");
        cliente.setEmail("ana@example.com");
        cliente.setTelefono("123456789");

        perro = new Perro();
        perro.setIdPerro(1L);
        perro.setNombre("Rocky");
        perro.setRaza("Labrador");
        perro.setEdad(3);
        perro.setSexo("Macho");
        perro.setCliente(cliente);

        planTrabajo = new PlanTrabajo();
        planTrabajo.setId(1L);
        planTrabajo.setNumeroSesiones(5);
        planTrabajo.setObservaciones("Sesiones de adaptación");
        planTrabajo.setPerro(perro);
        planTrabajo.setCliente(cliente);

        Actividad actividad = new Actividad();
        actividad.setNombre("Pasear 20 minutos");
        actividad.setDescripcion("Caminar en zona tranquila");
        actividad.setDuracion(20);
        actividad.setCompletado(false);

        Set<Actividad> actividades = new HashSet<>();
        actividades.add(actividad);
        planTrabajo.setActividades(actividades);
    }

    @Test
    void testGenerarInformePorPerro() throws Exception {
        when(perroRepository.findById(1L)).thenReturn(Optional.of(perro));
        when(planTrabajoRepository.findByPerro_IdPerro(1L)).thenReturn(Collections.singletonList(planTrabajo));

        byte[] pdf = informePdfService.generarInformePorPerro(1L);

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);

        verify(perroRepository).findById(1L);
        verify(planTrabajoRepository).findByPerro_IdPerro(1L);
    }

    @Test
    void testGenerarInformePerroNoExiste() {
        when(perroRepository.findById(99L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () ->
                informePdfService.generarInformePorPerro(99L));

        assertEquals("Perro no encontrado", ex.getMessage());
    }
}