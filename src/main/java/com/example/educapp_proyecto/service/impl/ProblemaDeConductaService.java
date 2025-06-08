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

/**
 * Servicio encargado de gestionar los problemas de conducta,
 * incluyendo su registro, asignación a perros y conversión a DTOs para el frontend.
 *
 * @author Noelia Vázquez Siles
 */
@Service
public class ProblemaDeConductaService implements ProblemaDeConductaServiceInterface {
    @Autowired
    private ProblemaDeConductaRepository problemaDeConductaRepository;

    @Autowired
    private PerroRepository perroRepository;


    /**
     * Recupera todos los problemas de conducta existentes en la base de datos.
     *
     * @return Lista de ProblemaDeConducta.
     */
    @Override
    public List<ProblemaDeConducta> findAll() {
        return problemaDeConductaRepository.findAll();
    }

    /**
     * Busca un problema de conducta por su ID.
     *
     * @param id ID del problema.
     * @return ProblemaDeConducta encontrado.
     * @throws RuntimeException si no se encuentra el problema.
     */
    @Override
    public ProblemaDeConducta findById(Long id) {
        Optional<ProblemaDeConducta> problemaDeConducta = problemaDeConductaRepository.findById(id);
        if (problemaDeConducta.isPresent()) {
            return problemaDeConducta.get();
        } else {
            throw new RuntimeException("Problema de conducta no encontrado con el id: " + id);
        }
    }

    // Guardar problema de conducta
    /**
     * Guarda un nuevo problema de conducta.
     *
     * @param problemaDeConducta objeto a guardar.
     * @return ProblemaDeConducta guardado.
     */
    @Override
    public ProblemaDeConducta save(ProblemaDeConducta problemaDeConducta) {
        return problemaDeConductaRepository.save(problemaDeConducta);
    }

    // Borrar problema por id
    /**
     * Borra un problema de conducta dado su ID.
     *
     * @param id ID del problema a eliminar.
     * @throws RuntimeException si no se encuentra el problema.
     */
    @Override
    public void deleteById(Long id) {
        if (problemaDeConductaRepository.existsById(id)) {
            problemaDeConductaRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, problema de conducta no encontrado con el id: " + id);
        }
    }

    // Actualizar un problema de conducta
    /**
     * Actualiza un problema de conducta existente.
     *
     * @param id ID del problema a actualizar.
     * @param problemaDeConducta nuevo objeto con los datos actualizados.
     * @return ProblemaDeConducta actualizado.
     * @throws RuntimeException si no se encuentra el problema.
     */
    public ProblemaDeConducta updateProblemaDeConducta(Long id, ProblemaDeConducta problemaDeConducta) {
        if (problemaDeConductaRepository.existsById(id)) {
            problemaDeConducta.setIdProblema(id);
            return problemaDeConductaRepository.save(problemaDeConducta);
        } else {
            throw new RuntimeException("Problema de conducta no encontrado con el id: " + id);
        }
    }

    // Asignar problemas por el id del perro
    /**
     * Asigna una lista de problemas de conducta a un perro específico.
     *
     * @param idPerro     ID del perro.
     * @param idProblemas Lista de IDs de los problemas a asignar.
     * @throws RuntimeException si no se encuentra el perro.
     */
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
    /**
     * Obtiene todos los problemas de conducta y los convierte en DTOs
     * que incluyen su ID, nombre, causas y soluciones.
     *
     * @return Lista de ProblemaConductaDto.
     */
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
    /**
     * Obtiene los problemas de conducta de un perro junto con sus causas y soluciones.
     *
     * @param idPerro ID del perro.
     * @return Lista de ProblemaConductaDto con la información relacionada.
     * @throws RuntimeException si no se encuentra el perro.
     */
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

