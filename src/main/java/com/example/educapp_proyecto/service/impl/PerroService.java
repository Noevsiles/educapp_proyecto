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
/**
 * Servicio que gestiona la lógica relacionada con la entidad Perro.
 * Incluye operaciones CRUD, asignación de problemas de conducta y conversión a DTOs.
 *
 * @author Noelia Vázquez Siles
 */
@Service
public class PerroService implements PerroServiceInterface {
    @Autowired
    private PerroRepository perroRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    private ProblemaDeConductaRepository problemaDeConductaRepository;


    // Encontrar todos los perros
    /**
     * Devuelve una lista con todos los perros registrados.
     *
     * @return lista de perros
     */
    @Override
    public List<Perro> findAll() {
        return perroRepository.findAll();
    }

    // Encontrar perro por su id
    /**
     * Busca un perro por su ID.
     *
     * @param id ID del perro
     * @return el perro encontrado
     * @throws RuntimeException si no se encuentra el perro
     */
    @Override
    public Perro findById(Long id) {
        Optional<Perro> perro = perroRepository.findById(id);
        if (perro.isPresent()) {
            return perro.get();
        } else {
            throw new RuntimeException("Perro no encontrado con el id: " + id);
        }
    }

    // Guardar perro
    /**
     * Guarda un nuevo perro.
     *
     * @param perro objeto perro a guardar
     * @return el perro guardado
     */
    @Override
    public Perro save(Perro perro) {
        return perroRepository.save(perro);
    }

    // Eliminar perro por su id
    /**
     * Elimina un perro por su ID.
     *
     * @param id ID del perro a eliminar
     */
    @Override
    public void deleteById(Long id) {
        if (perroRepository.existsById(id)) {
            perroRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, perro no encontrado con el id: " + id);
        }
    }

    // Actualizar un perro
    /**
     * Actualiza un perro existente.
     *
     * @param id ID del perro
     * @param perro datos actualizados
     * @return perro actualizado
     */
    public Perro updatePerro(Long id, Perro perro) {
        if (perroRepository.existsById(id)) {
            perro.setIdPerro(id);  // Aseguramos que el ID es el mismo
            return perroRepository.save(perro);
        } else {
            throw new RuntimeException("Perro no encontrado con el id: " + id);
        }
    }

    // Crear un perro
    /**
     * Crea un nuevo perro a partir de un DTO.
     *
     * @param dto datos del perro
     * @return DTO de respuesta con los datos del perro creado
     */
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

        if (cliente.getEducador() != null) {
            response.setIdEducador(cliente.getEducador().getIdEducador());
        }

        return response;
    }

    // Convertir Perro en PerroResponseDto
    /**
     * Convierte un objeto Perro en un DTO de respuesta.
     *
     * @param perro objeto perro
     * @return DTO de respuesta
     */
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

        if (perro.getCliente() != null && perro.getCliente().getEducador() != null) {
            dto.setIdEducador(perro.getCliente().getEducador().getIdEducador());
        }

        return dto;
    }

    // Obtener los perros asociados al educador
    /**
     * Devuelve los perros registrados por un educador específico.
     *
     * @param emailEducador email del educador
     * @return lista de perros del educador
     */
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
    /**
     * Asigna una lista de problemas de conducta a un perro.
     *
     * @param idPerro ID del perro
     * @param idProblemas lista de IDs de problemas
     */
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
    /**
     * Convierte un objeto Perro a un DTO con detalle, incluyendo problemas, causas y soluciones.
     *
     * @param perro objeto perro
     * @return DTO detallado
     */
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
    /**
     * Devuelve los perros asociados a un cliente identificado por su email.
     *
     * @param emailCliente email del cliente
     * @return lista de perros
     */
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

            if (cliente.getEducador() != null) {
                dto.setIdEducador(cliente.getEducador().getIdEducador());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    // Actualizar un perro utilizando el dto
    /**
     * Actualiza los datos de un perro a partir de un DTO.
     *
     * @param id ID del perro
     * @param dto datos actualizados
     * @return DTO del perro actualizado
     */
    @Override
    public PerroResponseDto actualizarPerroDesdeDto(Long id, PerroRequestDto dto) {
        Perro perro = perroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perro no encontrado"));

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        perro.setNombre(dto.getNombre());
        perro.setRaza(dto.getRaza());
        perro.setSexo(dto.getSexo());
        perro.setEdad(dto.getEdad());
        perro.setEsterilizado(dto.isEsterilizado());
        perro.setImagenUrl(dto.getImagenUrl());
        perro.setCliente(cliente);

        Perro actualizado = perroRepository.save(perro);
        return convertirAPerroDto(actualizado);
    }
}
