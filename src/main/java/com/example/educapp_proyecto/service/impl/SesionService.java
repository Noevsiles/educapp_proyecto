package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.dto.*;
import com.example.educapp_proyecto.exception.HorarioOcupadoException;
import com.example.educapp_proyecto.model.*;
import com.example.educapp_proyecto.repository.*;
import com.example.educapp_proyecto.service.SesionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    private ActividadRepository actividadRepository;


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
        sesion.setPlanTrabajo(plan); // puede ser null

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

    // Reservar una sesion con un educador
    @Override
    public Sesion reservarSesion(ReservaSesionDto dto, String emailIgnorado) {
        Perro perro = perroRepository.findById(dto.getIdPerro())
                .orElseThrow(() -> new RuntimeException("Perro no encontrado"));

        PlanTrabajo plan = null;
        if (dto.getIdPlanTrabajo() != null) {
            plan = planTrabajoRepository.findById(dto.getIdPlanTrabajo())
                    .orElseThrow(() -> new RuntimeException("Plan de trabajo no encontrado"));

            if (!plan.getCliente().equals(perro.getCliente())) {
                throw new RuntimeException("El plan de trabajo no pertenece al cliente del perro");
            }
        }

        Educador educador = educadorRepository.findById(dto.getIdEducador())
                .orElseThrow(() -> new RuntimeException("Educador no encontrado por ID"));

        boolean haySolapamiento = sesionRepository.existsByEducadorAndFechaHora(educador, dto.getFechaHora());
        if (haySolapamiento) {
            throw new RuntimeException("El educador ya tiene una sesión en ese horario");
        }

        String tipoSesion = (plan != null && !plan.getActividades().isEmpty())
                ? plan.getActividades().iterator().next().getNombre()
                : "Sesión sin plan de trabajo";

        Sesion sesion = new Sesion();
        sesion.setEducador(educador);
        sesion.setCliente(perro.getCliente());
        sesion.setPerro(perro);
        sesion.setPlanTrabajo(plan); // puede ser null
        sesion.setFechaHora(dto.getFechaHora());
        sesion.setTipoSesion(tipoSesion);
        sesion.setObservaciones("");
        sesion.setRealizada(false);
        sesion.setAceptada(false);

        return sesionRepository.save(sesion);
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

    // Obtener sesiones del educador
    @Override
    public List<SesionResponseDto> obtenerSesionesPorEducador(String emailEducador) {
        Educador educador = educadorRepository.findByEmail(emailEducador)
                .orElseThrow(() -> new RuntimeException("Educador no encontrado"));
        List<Sesion> sesiones = sesionRepository.findByPerro_Cliente_Educador(educador);

        return sesiones.stream().map(sesion -> {
            SesionResponseDto dto = new SesionResponseDto();
            dto.setId(sesion.getIdSesion());
            dto.setNombrePerro(sesion.getPerro().getNombre());
            dto.setActividad(sesion.getTipoSesion());
            dto.setFechaHora(sesion.getFechaHora());
            dto.setRealizada(sesion.isRealizada());
            return dto;
        }).collect(Collectors.toList());
    }

    // Aceptar la sesion que pide el cliente
    @Override
    public void aceptarSesion(Long id) {
        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        sesion.setAceptada(true);
        sesionRepository.save(sesion);
    }

    // Obtener huecos disponibles del educador
    @Override
    public List<String> obtenerHuecosDisponibles(Long idEducador, LocalDate fecha) {
        DayOfWeek dayOfWeek = fecha.getDayOfWeek();
        DiaSemana diaSemana = convertirADiaSemanaEspanol(dayOfWeek);

        List<Disponibilidad> disponibilidadDia = disponibilidadRepository
                .findByEducador_IdEducadorAndDiaSemana(idEducador, diaSemana);

        List<Sesion> sesionesOcupadas = sesionRepository
                .findByEducadorAndFechaRange(idEducador, fecha.atStartOfDay(), fecha.atTime(LocalTime.MAX));

        Set<LocalTime> horasOcupadas = sesionesOcupadas.stream()
                .map(s -> s.getFechaHora().toLocalTime())
                .collect(Collectors.toSet());

        List<String> huecosLibres = new ArrayList<>();
        for (Disponibilidad d : disponibilidadDia) {
            for (LocalTime hora = d.getHoraInicio(); hora.isBefore(d.getHoraFin()); hora = hora.plusHours(1)) {
                if (!horasOcupadas.contains(hora)) {
                    huecosLibres.add(hora.toString().substring(0, 5));
                }
            }
        }

        return huecosLibres;
    }


    // Convertir el DayOfWeek en mi Enum de dias de la semana en español
    private DiaSemana convertirADiaSemanaEspanol(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> DiaSemana.LUNES;
            case TUESDAY -> DiaSemana.MARTES;
            case WEDNESDAY -> DiaSemana.MIERCOLES;
            case THURSDAY -> DiaSemana.JUEVES;
            case FRIDAY -> DiaSemana.VIERNES;
            case SATURDAY -> DiaSemana.SABADO;
            case SUNDAY -> DiaSemana.DOMINGO;
        };
    }

    // Rechazar una sesion de adiestramiento
    @Override
    public SesionResponseDto rechazarSesion(Long idSesion) {
        Sesion sesion = sesionRepository.findById(idSesion)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        sesion.setRechazada(true);
        sesion.setAceptada(false); // opcional por consistencia
        Sesion actualizada = sesionRepository.save(sesion);

        SesionResponseDto dto = new SesionResponseDto();
        dto.setId(actualizada.getIdSesion());
        dto.setNombrePerro(actualizada.getPerro().getNombre());
        dto.setActividad(actualizada.getTipoSesion());
        dto.setFechaHora(actualizada.getFechaHora());
        dto.setRealizada(actualizada.isRealizada());
        dto.setAceptada(actualizada.isAceptada());
        dto.setRechazada(actualizada.isRechazada());
        return dto;
    }



}
