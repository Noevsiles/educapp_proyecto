package com.example.educapp_proyecto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado del envío de correos electrónicos,
 * como recordatorios y notificaciones generales.
 * Utiliza el componente JavaMailSender para enviar mensajes simples.
 *
 * @author Noelia Vázquez Siles
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Enviar recordatorio
    /**
     * Envía un correo electrónico con el propósito de ser un recordatorio.
     *
     * @param para dirección de correo del destinatario.
     * @param asunto asunto del correo.
     * @param cuerpo cuerpo del mensaje.
     */
    public void enviarRecordatorio(String para, String asunto, String cuerpo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(para);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);
        mailSender.send(mensaje);
    }

    // Enviar correo
    /**
     * Envía un correo electrónico genérico con los parámetros proporcionados.
     *
     * @param to dirección del destinatario.
     * @param subject asunto del correo.
     * @param text cuerpo del mensaje.
     */
    public void enviarCorreo(String to, String subject, String text) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(to);
        mensaje.setSubject(subject);
        mensaje.setText(text);
        mailSender.send(mensaje);
    }
}
