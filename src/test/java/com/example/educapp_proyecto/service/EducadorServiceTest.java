package com.example.educapp_proyecto.service;

import com.example.educapp_proyecto.model.Educador;
import com.example.educapp_proyecto.repository.EducadorRepository;
import com.example.educapp_proyecto.service.impl.EducadorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class EducadorServiceTest {
    @Mock
    private EducadorRepository educadorRepository;

    @InjectMocks
    private EducadorService educadorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para comprobar que se hace un update
    @Test
    void testUpdateEducador_existente() {
        Long id = 1L;
        Educador educador = new Educador();
        educador.setNombre("Antiguo");

        when(educadorRepository.existsById(id)).thenReturn(true);
        when(educadorRepository.save(any(Educador.class))).thenAnswer(inv -> inv.getArgument(0));

        Educador resultado = educadorService.updateEducador(id, educador);

        assertEquals(id, resultado.getIdEducador());
        verify(educadorRepository).existsById(id);
        verify(educadorRepository).save(educador);
    }

    @Test
    void testUpdateEducador_noExiste() {
        Long id = 99L;
        Educador educador = new Educador();
        when(educadorRepository.existsById(id)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            educadorService.updateEducador(id, educador);
        });

        assertEquals("Educador no encontrado con el id: " + id, ex.getMessage());
        verify(educadorRepository).existsById(id);
        verify(educadorRepository, never()).save(any());
    }
}
