package com.example.educapp_proyecto.service;


import com.example.educapp_proyecto.service.impl.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    // Test para enviar correos
    @Test
    void testEnviarCorreo() {
        // Datos de prueba
        String to = "cliente@ejemplo.com";
        String subject = "Asunto de prueba";
        String body = "Cuerpo del mensaje";

        // Ejecutar el metodo
        emailService.enviarCorreo(to, subject, body);

        // Capturar el mensaje enviado
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage enviado = captor.getValue();

        // Verificar contenido
        assertEquals(to, enviado.getTo()[0]);
        assertEquals(subject, enviado.getSubject());
        assertEquals(body, enviado.getText());
    }

    // Test para enviar recordatorio de que se tiene una sesion
    @Test
    void testEnviarRecordatorio() {
        // Igual que enviarCorreo, se puede testear de la misma forma
        String para = "usuario@ejemplo.com";
        String asunto = "Recordatorio";
        String cuerpo = "Recuerda tu sesión";

        emailService.enviarRecordatorio(para, asunto, cuerpo);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage enviado = captor.getValue();

        assertEquals(para, enviado.getTo()[0]);
        assertEquals(asunto, enviado.getSubject());
        assertEquals(cuerpo, enviado.getText());
    }
}
