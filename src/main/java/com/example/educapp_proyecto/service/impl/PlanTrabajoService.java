package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.dto.PlanTrabajoDto;
import com.example.educapp_proyecto.dto.PlanTrabajoRespuestaDto;
import com.example.educapp_proyecto.model.Actividad;
import com.example.educapp_proyecto.model.Cliente;
import com.example.educapp_proyecto.model.PlanTrabajo;
import com.example.educapp_proyecto.model.ProblemaDeConducta;
import com.example.educapp_proyecto.repository.ActividadRepository;
import com.example.educapp_proyecto.repository.ClienteRepository;
import com.example.educapp_proyecto.repository.PlanTrabajoRepository;
import com.example.educapp_proyecto.repository.ProblemaDeConductaRepository;
import com.example.educapp_proyecto.service.PlanTrabajoServiceInterface;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.hibernate.Hibernate;

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
            actividad.getPlanesTrabajo().add(plan);  // ¡IMPORTANTE!
        }
        PlanTrabajo planGuardado = planTrabajoRepository.save(plan);
        actividadRepository.saveAll(actividades);

        return planGuardado;
    }

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

        return dto;
    }



    @Transactional
    @Override
    public List<PlanTrabajoRespuestaDto> listarPlanesPorCliente(Long idCliente) {
        List<PlanTrabajo> planes = planTrabajoRepository.findByClienteIdWithRelations(idCliente);
        return planes.stream()
                .map(this::convertirAPlanDto)
                .collect(Collectors.toList());
    }


    @Override
    public PlanTrabajo buscarPorId(Long id) {
        return planTrabajoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plan no encontrado"));
    }


}