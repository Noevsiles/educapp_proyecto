package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.dto.HuecoAgendaCompletoDto;
import com.example.educapp_proyecto.dto.HuecoAgendaDto;
import com.example.educapp_proyecto.dto.ReservaSesionDto;
import com.example.educapp_proyecto.dto.SesionRequestDto;
import com.example.educapp_proyecto.model.*;
import com.example.educapp_proyecto.repository.*;
import com.example.educapp_proyecto.service.SesionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SesionService implements SesionServiceInterface {

    @Autowired
    private SesionRepository sesionRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EducadorRepository educadorRepository;

    @Autowired
    private PerroRepository perroRepository;

    @Autowired
    private PlanTrabajoRepository planTrabajoRepository;

    @Autowired
    private DisponibilidadRepository disponibilidadRepository;

    @Autowired
    private RecordatorioService recordatorioService;


    // Crear sesion
    @Override
    public Sesion crearSesion(SesionRequestDto dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Educador educador = educadorRepository.findById(dto.getEducadorId())
                .orElseThrow(() -> new RuntimeException("Educador no encontrado"));

        Perro perro = null;
        if (dto.getPerroId() != null) {
            perro = perroRepository.findById(dto.getPerroId())
                    .orElseThrow(() -> new RuntimeException("Perro no encontrado"));
        }

        PlanTrabajo plan = null;
        if (dto.getPlanTrabajoId() != null) {
            plan = planTrabajoRepository.findById(dto.getPlanTrabajoId())
                    .orElseThrow(() -> new RuntimeException("Plan de trabajo no encontrado"));
        }

        Sesion sesion = new Sesion();
        sesion.setFechaHora(dto.getFechaHora());
        sesion.setTipoSesion(dto.getTipoSesion());
        sesion.setObservaciones(dto.getObservaciones());
        sesion.setRealizada(false);
        sesion.setCliente(cliente);
        sesion.setEducador(educador);
        sesion.setPerro(perro);
        sesion.setPlanTrabajo(plan);

        return sesionRepository.save(sesion);
    }

    @Override
    public List<Sesion> findAll() {
        return sesionRepository.findAll();
    }

    @Override
    public Sesion findById(Long id) {
        Optional<Sesion> sesion = sesionRepository.findById(id);
        if (sesion.isPresent()) {
            return sesion.get();
        } else {
            throw new RuntimeException("Sesión no encontrada con el id: " + id);
        }
    }

    @Override
    public Sesion save(Sesion sesion) {
        return sesionRepository.save(sesion);
    }

    @Override
    public void deleteById(Long id) {
        if (sesionRepository.existsById(id)) {
            sesionRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, sesión no encontrada con el id: " + id);
        }
    }

    // Obtener los huecos disponibles para las sesiones
    @Override
    public List<HuecoAgendaDto> obtenerHuecosDisponibles(Long idEducador, LocalDate fecha) {
        DayOfWeek diaSemana = fecha.getDayOfWeek();

        // Busca directamente la disponibilidad para el día y educador
        List<Disponibilidad> disponibilidades = disponibilidadRepository.buscarPorEducadorYDia(idEducador, diaSemana);
        if (disponibilidades.isEmpty()) {
            return Collections.emptyList();
        }

        Disponibilidad disp = disponibilidades.get(0);
        LocalDateTime inicio = fecha.atTime(disp.getHoraInicio());
        LocalDateTime fin = fecha.atTime(disp.getHoraFin());

        List<Sesion> sesionesReservadas = sesionRepository
                .buscarPorEducadorIdYFechaHoraEntre(idEducador, inicio, fin);

        List<HuecoAgendaDto> huecos = new ArrayList<>();
        LocalDateTime huecoInicio = inicio;
        while (!huecoInicio.plusHours(1).isAfter(fin)) {
            LocalDateTime huecoFin = huecoInicio.plusHours(1);
            final LocalDateTime horaActual = huecoInicio;

            boolean ocupado = sesionesReservadas.stream()
                    .anyMatch(s -> s.getFechaHora().equals(horaActual));
            if (!ocupado) {
                huecos.add(new HuecoAgendaDto(horaActual, huecoFin, ocupado));
            }

            huecoInicio = huecoFin;
        }

        return huecos;
    }

    @Override
    public Sesion reservarSesion(ReservaSesionDto reserva) {
        // Compruebo si ya hay una sesión en el horario elegido con ese educador
        List<Sesion> ocupadas = sesionRepository.buscarPorEducadorIdYFechaHoraEntre(
                reserva.getIdEducador(),
                reserva.getFechaHora(),
                reserva.getFechaHora()
        );
        if (!ocupadas.isEmpty()) {
            throw new RuntimeException("Ese horario ya está reservado.");
        }

        Sesion nueva = new Sesion();
        nueva.setFechaHora(reserva.getFechaHora());
        nueva.setTipoSesion("Reserva");
        nueva.setRealizada(false);
        nueva.setObservaciones("");

        // Lanzo una excepción si no se encuentra el cliente, el perro, el educador o el plan de trabajo
        Cliente cliente = clienteRepository.findById(reserva.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Perro perro = perroRepository.findById(reserva.getIdPerro())
                .orElseThrow(() -> new RuntimeException("Perro no encontrado"));
        Educador educador = educadorRepository.findById(reserva.getIdEducador())
                .orElseThrow(() -> new RuntimeException("Educador no encontrado"));
        PlanTrabajo plan = planTrabajoRepository.findById(reserva.getIdPlanTrabajo())
                .orElseThrow(() -> new RuntimeException("Plan de trabajo no encontrado"));

        nueva.setCliente(cliente);
        nueva.setPerro(perro);
        nueva.setEducador(educador);
        nueva.setPlanTrabajo(plan);

        return sesionRepository.save(nueva);
    }

    // Obtener la agenda completa de un educador
    @Override
    public List<HuecoAgendaCompletoDto> obtenerAgendaCompleta(Long idEducador, LocalDate fecha) {
        DayOfWeek diaSemana = fecha.getDayOfWeek();
        List<Disponibilidad> disponibilidades = disponibilidadRepository.buscarPorEducadorYDia(idEducador, diaSemana);
        if (disponibilidades.isEmpty()) {
            return Collections.emptyList();
        }

        Disponibilidad disp = disponibilidades.get(0);
        LocalDateTime inicio = fecha.atTime(disp.getHoraInicio());
        LocalDateTime fin = fecha.atTime(disp.getHoraFin());

        List<Sesion> sesionesReservadas = sesionRepository.buscarPorEducadorIdYFechaHoraEntre(idEducador, inicio, fin);

        List<HuecoAgendaCompletoDto> huecos = new ArrayList<>();
        LocalDateTime huecoInicio = inicio;

        while (!huecoInicio.plusHours(1).isAfter(fin)) {
            LocalDateTime huecoFin = huecoInicio.plusHours(1);
            final LocalDateTime hora = huecoInicio;
            boolean ocupado = sesionesReservadas.stream()
                    .anyMatch(s -> s.getFechaHora().equals(hora));
            huecos.add(new HuecoAgendaCompletoDto(hora, huecoFin, ocupado));
            huecoInicio = huecoFin;
        }

        return huecos;
    }

    // Marcar sesiones como realizadas
    @Override
    public Sesion marcarComoRealizada(Long idSesion) {
        Sesion sesion = sesionRepository.findById(idSesion)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        sesion.setRealizada(true);
        return sesionRepository.save(sesion);
    }

    // Filtrar sesiones por cliente, perro o educador
    @Override
    public List<Sesion> filtrarSesiones(Long clienteId, Long perroId, Long educadorId) {
        return sesionRepository.findAll().stream()
                .filter(s -> (clienteId == null || s.getCliente().getIdCliente().equals(clienteId)))
                .filter(s -> (perroId == null || s.getPerro().getIdPerro().equals(perroId)))
                .filter(s -> (educadorId == null || s.getEducador().getIdEducador().equals(educadorId)))
                .collect(Collectors.toList());
    }

    // Enviar recordatorios de sesiones por email
    @Override
    public void enviarRecordatorios() {
        // Busca sesiones dentro de las próximas 24 horas
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime maniana = ahora.plusHours(24);

        List<Sesion> proximas = sesionRepository
                .findAll()
                .stream()
                .filter(s -> !s.isRealizada())
                .filter(s -> s.getFechaHora().isAfter(ahora) && s.getFechaHora().isBefore(maniana))
                .collect(Collectors.toList());

        for (Sesion sesion : proximas) {
            recordatorioService.enviarRecordatorio(sesion);
        }
    }
}
