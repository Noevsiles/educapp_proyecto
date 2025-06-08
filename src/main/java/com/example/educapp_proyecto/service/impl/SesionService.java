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

/**
 * Servicio que gestiona la lógica de negocio para las sesiones entre educadores y clientes.
 * Incluye operaciones para crear, reservar, aceptar, rechazar, filtrar sesiones,
 * y generar huecos disponibles en la agenda de un educador.
 *
 * @author Noelia Vázquez Siles
 */
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
    /**
     * Crea una nueva sesión.
     *
     * @param dto datos necesarios para crear la sesión
     * @return la sesión creada
     */
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

    // Encontrar todas las sesiones
    /**
     * Recupera todas las sesiones.
     *
     * @return lista de sesiones
     */
    @Override
    public List<Sesion> findAll() {
        return sesionRepository.findAll();
    }

    // Encontrar sesion por id
    /**
     * Busca una sesión por su ID.
     *
     * @param id identificador de la sesión
     * @return la sesión encontrada
     * @throws RuntimeException si no se encuentra la sesión
     */
    @Override
    public Sesion findById(Long id) {
        Optional<Sesion> sesion = sesionRepository.findById(id);
        if (sesion.isPresent()) {
            return sesion.get();
        } else {
            throw new RuntimeException("Sesión no encontrada con el id: " + id);
        }
    }

    // Guardar sesion
    /**
     * Guarda una sesión en la base de datos.
     *
     * @param sesion objeto de sesión a guardar
     * @return sesión guardada
     */
    @Override
    public Sesion save(Sesion sesion) {
        return sesionRepository.save(sesion);
    }

    // Borrar sesion por id
    /**
     * Elimina una sesión por su ID.
     *
     * @param id identificador de la sesión
     * @throws RuntimeException si no se encuentra la sesión
     */
    @Override
    public void deleteById(Long id) {
        if (sesionRepository.existsById(id)) {
            sesionRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, sesión no encontrada con el id: " + id);
        }
    }

    // Reservar una sesion con un educador
    /**
     * Reserva una sesión con un educador.
     *
     * @param dto datos necesarios para la reserva
     * @param emailIgnorado no se utiliza actualmente
     * @return sesión reservada
     */
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
    /**
     * Obtiene la agenda completa de un educador en una fecha específica.
     *
     * @param idEducador identificador del educador
     * @param fecha fecha para consultar
     * @return lista de huecos con estado ocupado/libre
     */
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
    /**
     * Marca una sesión como realizada.
     *
     * @param idSesion identificador de la sesión
     * @return sesión actualizada como realizada
     */
    @Override
    public Sesion marcarComoRealizada(Long idSesion) {
        Sesion sesion = sesionRepository.findById(idSesion)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        sesion.setRealizada(true);
        return sesionRepository.save(sesion);
    }

    // Filtrar sesiones por cliente, perro o educador
    /**
     * Filtra las sesiones por cliente, perro o educador.
     *
     * @param clienteId id del cliente
     * @param perroId id del perro
     * @param educadorId id del educador
     * @return lista filtrada de sesiones
     */
    @Override
    public List<Sesion> filtrarSesiones(Long clienteId, Long perroId, Long educadorId) {
        return sesionRepository.findAll().stream()
                .filter(s -> (clienteId == null || s.getCliente().getIdCliente().equals(clienteId)))
                .filter(s -> (perroId == null || s.getPerro().getIdPerro().equals(perroId)))
                .filter(s -> (educadorId == null || s.getEducador().getIdEducador().equals(educadorId)))
                .collect(Collectors.toList());
    }

    // Enviar recordatorios de sesiones por email
    /**
     * Envía recordatorios de sesiones a realizarse en las próximas 24 horas.
     */
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
    /**
     * Obtiene las sesiones programadas por un educador.
     *
     * @param emailEducador email del educador
     * @return lista de sesiones en forma de DTO
     */
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
    /**
     * Acepta una solicitud de sesión.
     *
     * @param id identificador de la sesión
     */
    @Override
    public void aceptarSesion(Long id) {
        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        sesion.setAceptada(true);
        sesion.setRechazada(false);
        sesionRepository.save(sesion);
    }

    // Obtener huecos disponibles del educador
    /**
     * Devuelve una lista de horas disponibles en un día para un educador.
     *
     * @param idEducador identificador del educador
     * @param fecha fecha a consultar
     * @return lista de horas en formato HH:mm
     */
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
    /**
     * Convierte un objeto DayOfWeek a un valor del enum DiaSemana.
     *
     * @param dayOfWeek día de la semana en inglés
     * @return día de la semana en español (enum)
     */
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
    /**
     * Rechaza una sesión solicitada por un cliente.
     *
     * @param idSesion identificador de la sesión
     * @return DTO con la información de la sesión rechazada
     */
    @Override
    public SesionResponseDto rechazarSesion(Long idSesion) {
        Sesion sesion = sesionRepository.findById(idSesion)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        sesion.setRechazada(true);
        sesion.setAceptada(false);
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

    // Obtener la lista de sesiones del cliente
    /**
     * Obtiene las sesiones programadas de un cliente.
     *
     * @param emailCliente email del cliente
     * @return lista de sesiones en forma de DTO
     */
    @Override
    public List<SesionClienteResponseDto> obtenerSesionesPorCliente(String emailCliente) {
        Cliente cliente = clienteRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<Sesion> sesiones = sesionRepository.findByCliente(cliente);

        return sesiones.stream().map(sesion -> {
            SesionClienteResponseDto dto = new SesionClienteResponseDto();
            dto.setId(sesion.getIdSesion());
            dto.setNombrePerro(sesion.getPerro().getNombre());
            dto.setActividad(sesion.getTipoSesion());
            dto.setFechaHora(sesion.getFechaHora());
            dto.setAceptada(sesion.isAceptada());
            dto.setRechazada(sesion.isRechazada());
            dto.setRealizada(sesion.isRealizada());
            return dto;
        }).collect(Collectors.toList());
    }
}
