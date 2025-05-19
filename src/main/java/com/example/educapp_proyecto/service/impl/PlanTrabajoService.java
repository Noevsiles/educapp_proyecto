package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.dto.PlanTrabajoDto;
import com.example.educapp_proyecto.dto.PlanTrabajoRespuestaDto;
import com.example.educapp_proyecto.model.*;
import com.example.educapp_proyecto.repository.ActividadRepository;
import com.example.educapp_proyecto.repository.ClienteRepository;
import com.example.educapp_proyecto.repository.PlanTrabajoRepository;
import com.example.educapp_proyecto.repository.ProblemaDeConductaRepository;
import com.example.educapp_proyecto.service.PlanTrabajoServiceInterface;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlanTrabajoService implements PlanTrabajoServiceInterface {

    @Autowired
    private PlanTrabajoRepository planTrabajoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProblemaDeConductaRepository problemaRepository;

    @Autowired
    private ActividadRepository actividadRepository;

    // Crear un plan de trabajo
    @Override
    @Transactional
    public PlanTrabajo crearPlan(PlanTrabajoDto dto) {
        // Buscar cliente
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Obtener problemas
        List<Long> problemaIds = dto.getProblemaIds().stream().distinct().toList();
        Set<ProblemaDeConducta> problemas = new HashSet<>(problemaRepository.findAllById(problemaIds));

        // Obtener actividades
        List<Long> actividadIds = dto.getActividadIds().stream().distinct().toList();
        Set<Actividad> actividades = new HashSet<>(actividadRepository.findAllById(actividadIds));

        // Crear el plan
        PlanTrabajo plan = new PlanTrabajo();
        plan.setCliente(cliente);
        plan.setObservaciones(dto.getObservaciones());
        plan.setProblemas(problemas);
        plan.setActividades(actividades);

        // Enlazar manualmente la relación inversa
        for (Actividad actividad : actividades) {
            actividad.getPlanesTrabajo().add(plan);  // IMPORTANTE!
        }
        PlanTrabajo planGuardado = planTrabajoRepository.save(plan);
        actividadRepository.saveAll(actividades);

        return planGuardado;
    }

    // Convertir el plan de trabajo a DTO
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
    @Transactional
    @Override
    public List<PlanTrabajoRespuestaDto> listarPlanesPorCliente(Long idCliente) {
        List<PlanTrabajo> planes = planTrabajoRepository.findByClienteIdWithRelations(idCliente);
        return planes.stream()
                .map(this::convertirAPlanDto)
                .collect(Collectors.toList());
    }

    // Buscar plan de trabajo por su id
    @Override
    public PlanTrabajo buscarPorId(Long id) {
        return planTrabajoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Plan no encontrado"));
    }

    // Guardar plan de trabajo
    public PlanTrabajo save(PlanTrabajo plan) {
        return planTrabajoRepository.save(plan);
    }

    // Obtener todos los planes de trabajo
    public List<PlanTrabajo> obtenerTodos() {
        return planTrabajoRepository.findAll();
    }

    // Eliminar un plan de trabajo por id
    @Override
    public void eliminarPorId(Long id) {
        planTrabajoRepository.deleteById(id);
    }

}