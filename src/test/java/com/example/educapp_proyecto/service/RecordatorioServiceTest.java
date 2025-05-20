package com.example.educapp_proyecto.service;


import com.example.educapp_proyecto.model.Cliente;
import com.example.educapp_proyecto.model.Educador;
import com.example.educapp_proyecto.model.Sesion;
import com.example.educapp_proyecto.repository.SesionRepository;
import com.example.educapp_proyecto.service.impl.EmailService;
import com.example.educapp_proyecto.service.impl.RecordatorioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecordatorioServiceTest {
    @Mock
    private SesionRepository sesionRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private RecordatorioService recordatorioService;

    private Sesion sesionValida;

    @BeforeEach
    void setUp() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");
        cliente.setEmail("juan@example.com");

        Educador educador = new Educador();
        educador.setNombre("Laura");

        sesionValida = new Sesion();
        sesionValida.setCliente(cliente);
        sesionValida.setEducador(educador);
        sesionValida.setFechaHora(LocalDateTime.now().plusHours(2));
        sesionValida.setRealizada(false);
    }

    // Test para enviar recordatorios cuando corresponda
    @Test
    void testEnviarRecordatorios_filtraYSoloEnviaCuandoCorresponde() {
        Sesion sesionPasada = new Sesion();
        sesionPasada.setFechaHora(LocalDateTime.now().minusHours(1));
        sesionPasada.setRealizada(false);

        Sesion sesionYaRealizada = new Sesion();
        sesionYaRealizada.setFechaHora(LocalDateTime.now().plusHours(1));
        sesionYaRealizada.setRealizada(true);

        List<Sesion> todas = Arrays.asList(sesionValida, sesionPasada, sesionYaRealizada);
        when(sesionRepository.findAll()).thenReturn(todas);

        recordatorioService.enviarRecordatorios();

        verify(emailService, times(1)).enviarRecordatorio(
                eq("juan@example.com"),
                eq("Recordatorio de sesión"),
                contains("Hola Juan")
        );
    }

    // Test para enviar recordatorio por una sesion
    @Test
    void testEnviarRecordatorio_conSesionIndividual() {
        recordatorioService.enviarRecordatorio(sesionValida);

        verify(emailService).enviarCorreo(
                eq("juan@example.com"),
                eq("Recordatorio de sesión con educador"),
                contains("Hola Juan")
        );
    }
}
