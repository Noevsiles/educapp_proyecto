package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.dto.CausaDto;
import com.example.educapp_proyecto.dto.ProblemaConductaDto;
import com.example.educapp_proyecto.dto.SolucionDto;
import com.example.educapp_proyecto.model.Causa;
import com.example.educapp_proyecto.model.Perro;
import com.example.educapp_proyecto.model.ProblemaDeConducta;
import com.example.educapp_proyecto.model.Solucion;
import com.example.educapp_proyecto.repository.PerroRepository;
import com.example.educapp_proyecto.repository.ProblemaDeConductaRepository;
import com.example.educapp_proyecto.service.ProblemaDeConductaServiceInterface;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProblemaDeConductaService implements ProblemaDeConductaServiceInterface {
    @Autowired
    private ProblemaDeConductaRepository problemaDeConductaRepository;

    @Autowired
    private PerroRepository perroRepository;


    @Override
    public List<ProblemaDeConducta> findAll() {
        return problemaDeConductaRepository.findAll();
    }

    @Override
    public ProblemaDeConducta findById(Long id) {
        Optional<ProblemaDeConducta> problemaDeConducta = problemaDeConductaRepository.findById(id);
        if (problemaDeConducta.isPresent()) {
            return problemaDeConducta.get();
        } else {
            throw new RuntimeException("Problema de conducta no encontrado con el id: " + id);
        }
    }

    @Override
    public ProblemaDeConducta save(ProblemaDeConducta problemaDeConducta) {
        return problemaDeConductaRepository.save(problemaDeConducta);
    }

    @Override
    public void deleteById(Long id) {
        if (problemaDeConductaRepository.existsById(id)) {
            problemaDeConductaRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, problema de conducta no encontrado con el id: " + id);
        }
    }

    // Actualizar un problema de conducta
    public ProblemaDeConducta updateProblemaDeConducta(Long id, ProblemaDeConducta problemaDeConducta) {
        if (problemaDeConductaRepository.existsById(id)) {
            problemaDeConducta.setIdProblema(id);
            return problemaDeConductaRepository.save(problemaDeConducta);
        } else {
            throw new RuntimeException("Problema de conducta no encontrado con el id: " + id);
        }
    }

    @Override
    @Transactional
    public void asignarProblemasAPerro(Long idPerro, List<Long> idProblemas) {
        Perro perro = perroRepository.findById(idPerro)
                .orElseThrow(() -> new RuntimeException("Perro no encontrado"));

        List<ProblemaDeConducta> problemas = problemaDeConductaRepository.findAllById(idProblemas);
        perro.setProblemasDeConducta(new HashSet<>(problemas));
        perroRepository.save(perro);
    }

    // Obtener los problemas de conducta solo con su id y su nombre
    @Override
    public List<ProblemaConductaDto> obtenerTodosSoloIdYNombre() {
        return problemaDeConductaRepository.findAll()
                .stream()
                .map(p -> {
                    // Convertir causas
                    List<CausaDto> causas = p.getCausaDeProblemas().stream()
                            .map(cdp -> {
                                Causa causa = cdp.getCausa();
                                return new CausaDto(causa.getIdCausa(), causa.getDescripcion());
                            })
                            .collect(Collectors.toList());

                    // Convertir soluciones
                    List<SolucionDto> soluciones = p.getSolucionAplicadas().stream()
                            .map(sa -> new SolucionDto(
                                    sa.getIdSolucionAplicada(),
                                    sa.getSolucion().getNombre(),
                                    sa.getSolucion().getDificultad(),
                                    sa.getSolucion().getDescripcion()
                            ))
                            .collect(Collectors.toList());

                    return new ProblemaConductaDto(
                            p.getIdProblema(),
                            p.getNombre(),
                            p.getDescripcion(),
                            causas,
                            soluciones
                    );
                })
                .toList();
    }

    // Obtener problemas con soluciones de un perro
    @Override
    public List<ProblemaConductaDto> obtenerProblemasYSolucionesDelPerro(Long idPerro) {
        Perro perro = perroRepository.findById(idPerro)
                .orElseThrow(() -> new RuntimeException("Perro no encontrado"));

        Set<ProblemaDeConducta> problemas = perro.getProblemasDeConducta();

        return problemas.stream().map(p -> {
            ProblemaConductaDto dto = new ProblemaConductaDto();
            dto.setIdProblemaConducta(p.getIdProblema());
            dto.setNombre(p.getNombre());
            dto.setDescripcion(p.getDescripcion());

            // Causas
            List<CausaDto> causas = p.getCausaDeProblemas().stream().map(cp -> {
                Causa causa = cp.getCausa();
                CausaDto cdto = new CausaDto();
                cdto.setIdCausa(causa.getIdCausa());
                cdto.setDescripcion(causa.getDescripcion());
                return cdto;
            }).collect(Collectors.toList());
            dto.setCausas(causas);

            // Soluciones
            List<SolucionDto> soluciones = p.getSoluciones() != null ? p.getSoluciones().stream().map(s -> {
                SolucionDto sdto = new SolucionDto();
                sdto.setIdSolucion(s.getIdSolucion());
                sdto.setNombre(s.getNombre());
                sdto.setDescripcion(s.getDescripcion());
                return sdto;
            }).collect(Collectors.toList()) : new ArrayList<>();
            dto.setSoluciones(soluciones);

            return dto;
        }).collect(Collectors.toList());
    }

}

