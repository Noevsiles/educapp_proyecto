package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.dto.HuecoAgendaDto;
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
                huecos.add(new HuecoAgendaDto(horaActual, huecoFin));
            }

            huecoInicio = huecoFin;
        }

        return huecos;
    }

}
