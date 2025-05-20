package com.example.educapp_proyecto.service;

import com.example.educapp_proyecto.model.Actividad;
import com.example.educapp_proyecto.model.SolucionAplicada;
import com.example.educapp_proyecto.repository.ActividadRepository;
import com.example.educapp_proyecto.repository.SolucionAplicadaRepository;
import com.example.educapp_proyecto.service.impl.SolucionAplicadaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SolucionAplicadaServiceTest {

    @Mock
    private SolucionAplicadaRepository solucionAplicadaRepository;

    @Mock
    private ActividadRepository actividadRepository;

    @InjectMocks
    private SolucionAplicadaService solucionAplicadaService;

    private SolucionAplicada solucionAplicada;
    private Actividad actividad;

    @BeforeEach
    void setUp() {
        solucionAplicada = new SolucionAplicada();
        solucionAplicada.setIdSolucionAplicada(1L);

        actividad = new Actividad();
        actividad.setIdActividad(2L);
        actividad.setCompletado(false);
    }

    // Test para actualizar una solucion aplicada que existe
    @Test
    void testUpdateSolucionAplicada_existente() {
        when(solucionAplicadaRepository.existsById(1L)).thenReturn(true);
        when(solucionAplicadaRepository.save(any())).thenReturn(solucionAplicada);

        SolucionAplicada resultado = solucionAplicadaService.updateSolucionAplicada(1L, solucionAplicada);

        assertEquals(1L, resultado.getIdSolucionAplicada());
        verify(solucionAplicadaRepository).save(solucionAplicada);
    }

    // Test para actualizar una solucion aplicada que no existe
    @Test
    void testUpdateSolucionAplicada_noExistente() {
        when(solucionAplicadaRepository.existsById(1L)).thenReturn(false);

        Exception ex = assertThrows(RuntimeException.class, () ->
                solucionAplicadaService.updateSolucionAplicada(1L, solucionAplicada));

        assertEquals("Solución aplicada no encontrada con el id: 1", ex.getMessage());
    }

    // Test para agregar una actividad
    @Test
    void testAgregarActividad() {
        when(solucionAplicadaRepository.findById(1L)).thenReturn(Optional.of(solucionAplicada));
        when(actividadRepository.save(any())).thenReturn(actividad);

        Actividad resultado = solucionAplicadaService.agregarActividad(1L, actividad);

        assertEquals(actividad, resultado);
        assertEquals(solucionAplicada, actividad.getSolucionAplicada());
        verify(actividadRepository).save(actividad);
    }

    // Test para agregar una actividad a una solucion que no existe
    @Test
    void testAgregarActividad_solucionNoExiste() {
        when(solucionAplicadaRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () ->
                solucionAplicadaService.agregarActividad(1L, actividad));

        assertEquals("Solución aplicada no encontrada con el id: 1", ex.getMessage());
    }

    // Test para actualizar el progreso de unna actividad
    @Test
    void testActualizarProgreso() {
        when(actividadRepository.findById(2L)).thenReturn(Optional.of(actividad));

        solucionAplicadaService.actualizarProgreso(2L, true);

        assertTrue(actividad.isCompletado());
        verify(actividadRepository).save(actividad);
    }

    // Test para actualizar progreso de una actividad que no existe
    @Test
    void testActualizarProgreso_actividadNoExiste() {
        when(actividadRepository.findById(2L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RuntimeException.class, () ->
                solucionAplicadaService.actualizarProgreso(2L, true));

        assertEquals("Progreso de actividad no encontrado con el id: 2", ex.getMessage());
    }
}
