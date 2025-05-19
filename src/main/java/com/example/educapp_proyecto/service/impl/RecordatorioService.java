package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.Sesion;
import com.example.educapp_proyecto.repository.SesionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordatorioService {

    @Autowired
    private SesionRepository sesionRepository;

    @Autowired
    private ProblemaDeConductaService.EmailService emailService;

    // Revisa cada hora si hay sesiones que empiezan en 3h
    @Scheduled(cron = "0 0 * * * *")
    public void enviarRecordatorios() {
        System.out.println("Buscando sesiones para enviar recordatorios...");
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime dentroDeCincoMin = ahora.plusMinutes(5);

        List<Sesion> proximasSesiones = sesionRepository.findAll().stream()
                .filter(s -> !s.isRealizada())
                .filter(s -> s.getFechaHora().isAfter(ahora) && s.getFechaHora().isBefore(dentroDeCincoMin))
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
    public void enviarRecordatorio(Sesion sesion) {
        String destinatario = sesion.getCliente().getEmail();
        String asunto = "Recordatorio de sesión con educador";
        String cuerpo = "Hola " + sesion.getCliente().getNombre() + ",\n\n"
                + "Te recordamos que tienes una sesión programada con " + sesion.getEducador().getNombre()
                + " el día " + sesion.getFechaHora().toLocalDate() + " a las " + sesion.getFechaHora().toLocalTime() + ".\n\n"
                + "¡No faltes!";
        emailService.enviarCorreo(destinatario, asunto, cuerpo);
    }

}
