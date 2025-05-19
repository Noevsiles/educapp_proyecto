package com.example.educapp_proyecto.service;

import com.example.educapp_proyecto.model.Actividad;
import com.example.educapp_proyecto.repository.ActividadRepository;
import com.example.educapp_proyecto.service.impl.ActividadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ActividadServiceTest {

    @Mock
    private ActividadRepository actividadRepository;

    @InjectMocks
    private ActividadService actividadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para actualizar el estado de una actividad con id que existe
    @Test
    void testActualizarEstadoActividad_existente() {
        Long id = 1L;
        Actividad actividad = new Actividad();
        actividad.setIdActividad(id);
        actividad.setCompletado(false);

        when(actividadRepository.findById(id)).thenReturn(Optional.of(actividad));
        when(actividadRepository.save(any(Actividad.class))).thenAnswer(inv -> inv.getArgument(0));

        Actividad resultado = actividadService.actualizarEstadoActividad(id, true);

        assertTrue(resultado.isCompletado());
        verify(actividadRepository).findById(id);
        verify(actividadRepository).save(actividad);
    }

    // Test para actualizar el estado de una actividad con un id que no existe
    @Test
    void testActualizarEstadoActividad_noExiste() {
        when(actividadRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            actividadService.actualizarEstadoActividad(99L, true);
        });

        assertEquals("Actividad no encontrada con el id: 99", ex.getMessage());
        verify(actividadRepository).findById(99L);
        verify(actividadRepository, never()).save(any());
    }

    // Test para actualizar duracion de actividad con id que existe
    @Test
    void testActualizarDuracionActividad_existente() {
        Long id = 2L;
        Actividad actividad = new Actividad();
        actividad.setIdActividad(id);
        actividad.setDuracion(30);

        when(actividadRepository.findById(id)).thenReturn(Optional.of(actividad));
        when(actividadRepository.save(any(Actividad.class))).thenAnswer(inv -> inv.getArgument(0));

        Actividad resultado = actividadService.actualizarDuracionActividad(id, 60);

        assertEquals(60, resultado.getDuracion());
        verify(actividadRepository).findById(id);
        verify(actividadRepository).save(actividad);
    }

    // Test para actualizar duracion de actividad con id que no existe (excepcion)
    @Test
    void testActualizarDuracionActividad_noExiste() {
        when(actividadRepository.findById(123L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            actividadService.actualizarDuracionActividad(123L, 45);
        });

        assertEquals("Actividad no encontrada con el id: 123", ex.getMessage());
        verify(actividadRepository).findById(123L);
        verify(actividadRepository, never()).save(any());
    }
}
