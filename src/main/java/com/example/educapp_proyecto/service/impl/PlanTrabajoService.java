package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.component.PlanTrabajoMapper;
import com.example.educapp_proyecto.dto.*;
import com.example.educapp_proyecto.model.*;
import com.example.educapp_proyecto.repository.*;
import com.example.educapp_proyecto.service.PlanTrabajoServiceInterface;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los planes de trabajo,
 * incluyendo su creación, consulta, eliminación y asignación de actividades y soluciones.
 *
 * @author Noelia Vázquez Siles
 */
@Service
public class PlanTrabajoService implements PlanTrabajoServiceInterface {

    @Autowired
    private PerroRepository perroRepository;

    @Autowired
    private PlanTrabajoRepository planTrabajoRepository;

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private ProblemaDeConductaRepository problemaRepository;

    @Autowired
    private SolucionRepository solucionRepository;

    @Autowired
    private SolucionAplicadaRepository solucionAplicadaRepository;

    @Autowired
    private ProblemaDeConductaRepository problemaDeConductaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EducadorRepository educadorRepository;

    @Autowired
    private PlanTrabajoMapper planTrabajoMapper;


    // Crear un plan de trabajo
    /**
     * Crea un nuevo plan de trabajo para un cliente y un perro, asociando problemas, soluciones y actividades.
     *
     * @param dto DTO que contiene los datos necesarios para crear el plan.
     * @return PlanTrabajo creado con todas las relaciones asociadas.
     */
    @Transactional
    @Override
    public PlanTrabajo crearPlan(PlanTrabajoDto dto) {
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Perro perro = perroRepository.findById(dto.getIdPerro())
                .orElseThrow(() -> new RuntimeException("Perro no encontrado"));

        PlanTrabajo plan = new PlanTrabajo();
        plan.setCliente(cliente);
        plan.setPerro(perro); // si tienes esta relación
        plan.setObservaciones(dto.getObservaciones());
        plan.setNumeroSesiones(dto.getNumeroSesiones());

        // Asociar actividades generales al plan
        if (dto.getActividadIds() != null && !dto.getActividadIds().isEmpty()) {
            List<Actividad> actividades = actividadRepository.findAllById(dto.getActividadIds());
            plan.setActividades(new HashSet<>(actividades));
        }

        // Asociar problemas generales al plan
        if (dto.getProblemaIds() != null && !dto.getProblemaIds().isEmpty()) {
            List<ProblemaDeConducta> problemas = problemaDeConductaRepository.findAllById(dto.getProblemaIds());
            plan.setProblemas(new HashSet<>(problemas));
        }

        // Guardar el plan inicialmente
        planTrabajoRepository.save(plan);

        // Procesar soluciones aplicadas
        if (dto.getSolucionesAplicadas() != null && !dto.getSolucionesAplicadas().isEmpty()) {
            for (SolucionAplicadaRequest solReq : dto.getSolucionesAplicadas()) {
                ProblemaDeConducta problema = problemaDeConductaRepository.findById(solReq.getIdProblemaConducta())
                        .orElseThrow(() -> new RuntimeException("Problema de conducta no encontrado"));

                Solucion solucion = solucionRepository.findById(solReq.getIdSolucion())
                        .orElseThrow(() -> new RuntimeException("Solución no encontrada"));

                SolucionAplicada solucionAplicada = new SolucionAplicada();
                solucionAplicada.setPlanTrabajo(plan);
                solucionAplicada.setProblemaDeConducta(problema);
                solucionAplicada.setSolucion(solucion);
                solucionAplicada.setNumeroDeSesiones(solReq.getNumeroDeSesiones());

                // Actividades asociadas a esta solución
                if (solReq.getActividades() != null && !solReq.getActividades().isEmpty()) {
                    List<Actividad> actividades = solReq.getActividades().stream().map(actDto -> {
                        Actividad actividad = new Actividad();
                        actividad.setNombre(actDto.getNombre());
                        actividad.setDescripcion(actDto.getDescripcion());
                        actividad.setDuracion(actDto.getDuracion());
                        actividad.setCompletado(actDto.isCompletado());
                        actividad.setSolucionAplicada(solucionAplicada); // Relación bidireccional
                        return actividad;
                    }).collect(Collectors.toList());

                    solucionAplicada.setActividades(actividades);
                }

                solucionAplicadaRepository.save(solucionAplicada);
            }
        }

        // Retornar el plan actualizado con sus relaciones
        return planTrabajoRepository.findById(plan.getId())
                .orElseThrow(() -> new RuntimeException("Error al recuperar el plan"));
    }


    // Convertir el plan de trabajo a DTO
    /**
     * Convierte un objeto PlanTrabajo en su DTO de respuesta.
     *
     * @param plan PlanTrabajo a convertir.
     * @return DTO con los datos del plan.
     */
    private PlanTrabajoRespuestaDto convertirAPlanDto(PlanTrabajo plan) {
        PlanTrabajoRespuestaDto dto = new PlanTrabajoRespuestaDto();
        dto.setId(plan.getId());
        dto.setObservaciones(plan.getObservaciones());
        dto.setNombreCliente(plan.getCliente().getNombre());
        dto.setEmailCliente(plan.getCliente().getEmail());

        // Convertir nombres de problemas
        dto.setProblemas(
                plan.getProblemas().stream()
                        .map(ProblemaDeConducta::getNombre)
                        .collect(Collectors.toList())
        );

        // Convertir nombres de actividades
        dto.setActividades(
                plan.getActividades().stream()
                        .map(Actividad::getNombre)
                        .collect(Collectors.toList())
        );

        //Convertir nombres de perros
        dto.setNombresPerros(
                plan.getCliente().getPerros().stream()
                        .map(Perro::getNombre)
                        .collect(Collectors.toList())
        );


        return dto;
    }

    // Listar los planes de trabajo por cliente
    /**
     * Lista los planes de trabajo de un cliente específico.
     *
     * @param idCliente ID del cliente.
     * @return Lista de planes de trabajo en formato DTO.
     */
    @Transactional
    @Override
    public List<PlanTrabajoRespuestaDto> listarPlanesPorCliente(Long idCliente) {
        List<PlanTrabajo> planes = planTrabajoRepository.findByClienteIdWithRelations(idCliente);
        return planes.stream()
                .map(this::convertirAPlanDto)
                .collect(Collectors.toList());
    }

    // Buscar plan de trabajo por su id
    /**
     * Busca un plan de trabajo por su ID.
     *
     * @param id ID del plan.
     * @return PlanTrabajo encontrado.
     */
    @Override
    public PlanTrabajo buscarPorId(Long id) {
        return planTrabajoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Plan no encontrado"));
    }

    // Guardar plan de trabajo
    /**
     * Guarda un plan de trabajo.
     *
     * @param plan Plan de trabajo a guardar.
     * @return Plan de trabajo guardado.
     */
    public PlanTrabajo save(PlanTrabajo plan) {
        return planTrabajoRepository.save(plan);
    }

    // Obtener todos los planes de trabajo
    /**
     * Obtiene todos los planes de trabajo del sistema.
     *
     * @return Lista de todos los planes.
     */
    public List<PlanTrabajo> obtenerTodos() {
        return planTrabajoRepository.findAll();
    }

    // Eliminar un plan de trabajo por id
    /**
     * Elimina un plan de trabajo dado su ID.
     *
     * @param id ID del plan a eliminar.
     */
    @Override
    public void eliminarPorId(Long id) {
        planTrabajoRepository.deleteById(id);
    }

    // Obtener los planes de trabajo por cliente
    /**
     * Obtiene los planes de trabajo asociados a un cliente por su email.
     *
     * @param emailCliente Email del cliente.
     * @return Lista de planes en formato cliente DTO.
     */
    @Override
    public List<PlanTrabajoClienteDto> obtenerPlanesPorCliente(String emailCliente) {
        Cliente cliente = clienteRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<PlanTrabajo> planes = planTrabajoRepository.findByPerro_Cliente(cliente);

        return planes.stream().map(plan -> {
            PlanTrabajoClienteDto dto = new PlanTrabajoClienteDto();
            dto.setIdPlan(plan.getId());
            dto.setObservaciones(plan.getObservaciones());
            dto.setNumeroSesiones(plan.getNumeroSesiones());

            // Datos del perro
            Perro perro = plan.getPerro();
            dto.setPerro(new PerroMiniDto(perro.getIdPerro(), perro.getNombre()));

            // Problemas de conducta
            dto.setProblemas(plan.getProblemas().stream()
                    .map(p -> new ProblemaMiniDto(p.getIdProblema(), p.getNombre()))
                    .collect(Collectors.toList()));

            // Actividades: combinar las del plan y las de soluciones aplicadas
            List<Actividad> actividadesDesdeSoluciones = actividadRepository.findByPlanTrabajoIdFromSoluciones(plan.getId());

            Set<Actividad> actividadesTotales = new HashSet<>();
            actividadesTotales.addAll(plan.getActividades());              // actividades directas
            actividadesTotales.addAll(actividadesDesdeSoluciones);         // actividades desde soluciones

            dto.setActividades(actividadesTotales.stream()
                    .map(a -> new ActividadMiniDto(a.getIdActividad(), a.getNombre()))
                    .collect(Collectors.toList()));

            return dto;
        }).collect(Collectors.toList());
    }

    // Obtener los planes de trabajo por el educador
    /**
     * Obtiene los planes de trabajo de todos los clientes de un educador por su email.
     *
     * @param emailEducador Email del educador autenticado.
     * @return Lista de planes de trabajo del educador.
     */
    @Override
    public List<PlanTrabajoRespuestaDto> obtenerPlanesPorEducador(String emailEducador) {
        System.out.println("Buscando educador con email: " + emailEducador);

        Educador educador = educadorRepository.findByUsuarioEmail(emailEducador)
                .orElseThrow(() -> {
                    System.out.println("No se encontró el educador con ese email.");
                    return new RuntimeException("Educador no encontrado");
                });

        System.out.println("Educador encontrado: " + educador.getNombre());

        List<PlanTrabajo> planes = planTrabajoRepository.findByCliente_Educador(educador);

        System.out.println("Planes encontrados: " + planes.size());

        return planes.stream()
                .map(planTrabajoMapper::aDtoRespuesta)
                .collect(Collectors.toList());
    }

}