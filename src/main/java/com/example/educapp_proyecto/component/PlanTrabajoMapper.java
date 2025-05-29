package com.example.educapp_proyecto.component;

import com.example.educapp_proyecto.dto.PlanTrabajoRespuestaDto;
import com.example.educapp_proyecto.model.PlanTrabajo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlanTrabajoMapper {

    public PlanTrabajoRespuestaDto aDtoRespuesta(PlanTrabajo plan) {
        PlanTrabajoRespuestaDto dto = new PlanTrabajoRespuestaDto();
        dto.setId(plan.getId());
        dto.setObservaciones(plan.getObservaciones());
        dto.setNumeroSesiones(plan.getNumeroSesiones());

        if (plan.getCliente() != null) {
            dto.setNombreCliente(plan.getCliente().getNombre());
            dto.setEmailCliente(plan.getCliente().getEmail());
        }

        if (plan.getPerro() != null) {
            dto.setNombresPerros(List.of(plan.getPerro().getNombre()));
        }

        if (plan.getProblemas() != null) {
            List<String> nombresProblemas = plan.getProblemas().stream()
                    .map(p -> p.getNombre())
                    .collect(Collectors.toList());
            dto.setProblemas(nombresProblemas);
        }

        if (plan.getActividades() != null) {
            List<String> nombresActividades = plan.getActividades().stream()
                    .map(a -> a.getNombre())
                    .collect(Collectors.toList());
            dto.setActividades(nombresActividades);
        }

        return dto;
    }
}