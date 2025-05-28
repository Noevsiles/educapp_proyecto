package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.dto.*;
import com.example.educapp_proyecto.model.Cliente;
import com.example.educapp_proyecto.model.Perro;
import com.example.educapp_proyecto.model.ProblemaDeConducta;
import com.example.educapp_proyecto.repository.ClienteRepository;
import com.example.educapp_proyecto.repository.PerroRepository;
import com.example.educapp_proyecto.repository.ProblemaDeConductaRepository;
import com.example.educapp_proyecto.service.PerroServiceInterface;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PerroService implements PerroServiceInterface {
    @Autowired
    private PerroRepository perroRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    private ProblemaDeConductaRepository problemaDeConductaRepository;


    @Override
    public List<Perro> findAll() {
        return perroRepository.findAll();
    }

    @Override
    public Perro findById(Long id) {
        Optional<Perro> perro = perroRepository.findById(id);
        if (perro.isPresent()) {
            return perro.get();
        } else {
            throw new RuntimeException("Perro no encontrado con el id: " + id);
        }
    }

    @Override
    public Perro save(Perro perro) {
        return perroRepository.save(perro);
    }

    @Override
    public void deleteById(Long id) {
        if (perroRepository.existsById(id)) {
            perroRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, perro no encontrado con el id: " + id);
        }
    }

    // Actualizar un perro
    public Perro updatePerro(Long id, Perro perro) {
        if (perroRepository.existsById(id)) {
            perro.setIdPerro(id);  // Aseguramos que el ID es el mismo
            return perroRepository.save(perro);
        } else {
            throw new RuntimeException("Perro no encontrado con el id: " + id);
        }
    }

    // Crear un perro
    @Override
    public PerroResponseDto crearPerro(PerroRequestDto dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID " + dto.getClienteId()));

        Perro perro = new Perro();
        perro.setNombre(dto.getNombre());
        perro.setRaza(dto.getRaza());
        perro.setSexo(dto.getSexo());
        perro.setEdad(dto.getEdad());
        perro.setEsterilizado(dto.isEsterilizado());
        perro.setCliente(cliente);
        perro.setImagenUrl(dto.getImagenUrl());

        Perro guardado = perroRepository.save(perro);

        PerroResponseDto response = new PerroResponseDto();
        response.setIdPerro(guardado.getIdPerro());
        response.setNombre(guardado.getNombre());
        response.setRaza(guardado.getRaza());
        response.setSexo(guardado.getSexo());
        response.setEdad(guardado.getEdad());
        response.setEsterilizado(guardado.isEsterilizado());
        response.setNombreCliente(cliente.getNombre());
        perro.setImagenUrl(dto.getImagenUrl());

        return response;
    }

    // Convertir Perro en PerroResponseDto
    @Override
    public PerroResponseDto convertirAPerroDto(Perro perro) {
        PerroResponseDto dto = new PerroResponseDto();
        dto.setIdPerro(perro.getIdPerro());
        dto.setNombre(perro.getNombre());
        dto.setRaza(perro.getRaza());
        dto.setSexo(perro.getSexo());
        dto.setEdad(perro.getEdad());
        dto.setEsterilizado(perro.isEsterilizado());
        dto.setImagenUrl(perro.getImagenUrl());
        dto.setIdCliente(perro.getCliente().getIdCliente());
        dto.setNombreCliente(perro.getCliente().getNombre());
        return dto;
    }

    // Obtener los perros asociados al educador
    @Override
    public List<PerroResponseDto> obtenerPerrosPorEducador(String emailEducador) {
        List<Perro> perros = perroRepository.findByCliente_Educador_Email(emailEducador);

        return perros.stream().map(perro -> {
            PerroResponseDto dto = new PerroResponseDto();
            dto.setIdPerro(perro.getIdPerro());
            dto.setNombre(perro.getNombre());
            dto.setRaza(perro.getRaza());
            dto.setSexo(perro.getSexo());
            dto.setEdad(perro.getEdad());
            dto.setEsterilizado(perro.isEsterilizado());
            dto.setIdCliente(perro.getCliente().getIdCliente());
            dto.setNombreCliente(perro.getCliente().getNombre());
            dto.setImagenUrl(perro.getImagenUrl());
            return dto;
        }).collect(Collectors.toList());
    }


    // Asignar problemas a perro
    @Override
    @Transactional
    public void asignarProblemasA(Long idPerro, List<Long> idProblemas) {
        Perro perro = perroRepository.findById(idPerro)
                .orElseThrow(() -> new RuntimeException("Perro no encontrado"));

        List<ProblemaDeConducta> problemas = problemaDeConductaRepository.findAllById(idProblemas);
        perro.setProblemasDeConducta(new HashSet<>(problemas));

        perroRepository.save(perro);
    }

    // Convertir perro a dto
    @Override
    public PerroDetalleDto convertirADetalleDto(Perro perro) {
        PerroDetalleDto dto = new PerroDetalleDto();
        dto.setIdPerro(perro.getIdPerro());
        dto.setNombre(perro.getNombre());
        dto.setRaza(perro.getRaza());
        dto.setEdad(perro.getEdad());
        dto.setSexo(perro.getSexo());
        dto.setEsterilizado(perro.isEsterilizado());
        dto.setImagenUrl(perro.getImagenUrl());

        List<ProblemaConductaDto> problemas = perro.getProblemasDeConducta().stream().map(p -> {
            ProblemaConductaDto pdto = new ProblemaConductaDto();
            pdto.setIdProblemaConducta(p.getIdProblema());
            pdto.setNombre(p.getNombre());
            pdto.setDescripcion(p.getDescripcion());

            List<SolucionDto> soluciones = p.getSolucionAplicadas().stream().map(s -> {
                SolucionDto sdto = new SolucionDto();
                sdto.setIdSolucion(s.getIdSolucionAplicada());
                sdto.setNombre(s.getSolucion().getNombre());
                sdto.setDescripcion(s.getSolucion().getDescripcion());
                return sdto;
            }).collect(Collectors.toList());

            pdto.setSoluciones(soluciones);

            // Añadir causas asociadas al problema
            List<CausaDto> causas = p.getCausaDeProblemas().stream()
                    .map(causaDeProblema -> {
                        CausaDto cdto = new CausaDto();
                        cdto.setIdCausa(causaDeProblema.getCausa().getIdCausa());
                        cdto.setDescripcion(causaDeProblema.getCausa().getDescripcion());
                        return cdto;
                    })
                    .collect(Collectors.toList());

            pdto.setCausas(causas);

            return pdto;
        }).collect(Collectors.toList());

        dto.setProblemasDeConducta(problemas);

        return dto;
    }

    // Obtener los perros que tiene un cliente (puede tener mas de uno)
    @Override
    public List<PerroResponseDto> obtenerPerrosPorCliente(String emailCliente) {
        Cliente cliente = clienteRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        return cliente.getPerros().stream().map(perro -> {
            PerroResponseDto dto = new PerroResponseDto();
            dto.setIdPerro(perro.getIdPerro());
            dto.setNombre(perro.getNombre());
            dto.setRaza(perro.getRaza());
            dto.setEdad(perro.getEdad());
            dto.setSexo(perro.getSexo());
            dto.setEsterilizado(perro.isEsterilizado());
            dto.setImagenUrl(perro.getImagenUrl());
            dto.setIdCliente(cliente.getIdCliente());
            dto.setNombreCliente(cliente.getNombre());
            return dto;
        }).collect(Collectors.toList());
    }



}
