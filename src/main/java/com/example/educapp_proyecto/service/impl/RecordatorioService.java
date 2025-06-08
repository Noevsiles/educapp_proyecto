package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.Sesion;
import com.example.educapp_proyecto.repository.SesionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de enviar recordatorios por correo electrónico a los clientes
 * sobre sus sesiones próximas en EducApp.
 * Los recordatorios pueden ser enviados automáticamente (cada hora) o manualmente.
 *
 * @author Noelia Vázquez Siles
 */
@Service
public class RecordatorioService {

    @Autowired
    private SesionRepository sesionRepository;

    @Autowired
    private EmailService emailService;

    // Revisa cada hora si hay sesiones que empiezan en 3h
    /**
     * Tarea programada que se ejecuta cada hora.
     * Busca sesiones que comienzan dentro de 3 horas y aún no se han realizado,
     * y envía un recordatorio por correo electrónico al cliente correspondiente.
     */
    @Scheduled(cron = "0 0 * * * *")
    public void enviarRecordatorios() {
        System.out.println("Buscando sesiones para enviar recordatorios...");
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime enTresHoras = ahora.plusHours(3);;

        List<Sesion> proximasSesiones = sesionRepository.findAll().stream()
                .filter(s -> !s.isRealizada())
                .filter(s -> s.getFechaHora().isAfter(ahora) && s.getFechaHora().isBefore(enTresHoras))
                .collect(Collectors.toList());

        for (Sesion s : proximasSesiones) {
            String email = s.getCliente().getEmail();
            String asunto = "Recordatorio de sesión";
            String cuerpo = String.format("Hola %s, desde EducApp te recordamos que tienes una sesión programada para las %s.",
                    s.getCliente().getNombre(), s.getFechaHora().toString());
            emailService.enviarRecordatorio(email, asunto, cuerpo);
        }
    }

    // Enviar recordatorio a email
    /**
     * Envía un recordatorio por correo electrónico para una sesión específica.
     *
     * @param sesion La sesión para la cual se enviará el recordatorio.
     */
    public void enviarRecordatorio(Sesion sesion) {
        String destinatario = sesion.getCliente().getEmail();
        String asunto = "Recordatorio de sesión con educador";
        String cuerpo = "Hola " + sesion.getCliente().getNombre() + ",\n\n"
                + "Te recordamos que tienes una sesión programada con " + sesion.getEducador().getNombre()
                + " el día " + sesion.getFechaHora().toLocalDate() + " a las " + sesion.getFechaHora().toLocalTime() + ".\n\n"
                + "¡No faltes!";
        emailService.enviarCorreo(destinatario, asunto, cuerpo);
    }

    // Enviar recordatorios manualmente a todos los clientes con sesiones agendadas
    /**
     * Envia manualmente recordatorios por correo a todos los clientes que tengan sesiones pendientes
     * en el futuro y aún no realizadas.
     */
    public void enviarRecordatoriosManualmente() {
        List<Sesion> sesionesPendientes = sesionRepository.findAll().stream()
                .filter(s -> !s.isRealizada())
                .filter(s -> s.getFechaHora().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        for (Sesion sesion : sesionesPendientes) {
            enviarRecordatorio(sesion);
        }
    }
}
