package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.Causa;
import com.example.educapp_proyecto.repository.CausaRepository;
import com.example.educapp_proyecto.service.CausaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que gestiona las operaciones CRUD sobre causas relacionadas con problemas de conducta.
 * Proporciona métodos para buscar, guardar, actualizar y eliminar causas.
 *
 * @author Noelia Vázquez Siles
 */
@Service
public class CausaService implements CausaServiceInterface {

    @Autowired
    private CausaRepository causaRepository;

    /**
     * Obtiene todas las causas registradas en la base de datos.
     *
     * @return lista de {Causa}.
     */
    @Override
    public List<Causa> findAll() {
        return causaRepository.findAll();
    }

    // Encontrar una causa por su id
    /**
     * Busca una causa por su identificador.
     *
     * @param id identificador de la causa.
     * @return la causa encontrada.
     * @throws RuntimeException si no se encuentra la causa con el ID dado.
     */
    @Override
    public Causa findById(Long id) {
        Optional<Causa> causa = causaRepository.findById(id);
        if (causa.isPresent()) {
            return causa.get();
        } else {
            throw new RuntimeException("Causa no encontrada con el id: " + id);
        }
    }

    // Guardar una causa
    /**
     * Guarda una nueva causa en la base de datos.
     *
     * @param causa objeto a guardar.
     * @return la causa guardada.
     */
    @Override
    public Causa save(Causa causa) {
        return causaRepository.save(causa);
    }

    /**
     * Elimina una causa por su ID si existe.
     *
     * @param id identificador de la causa a eliminar.
     * @throws RuntimeException si la causa no existe.
     */
    @Override
    public void deleteById(Long id) {
        if (causaRepository.existsById(id)) {
            causaRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, causa no encontrada con el id: " + id);
        }
    }

    // Actualizar una causa
    /**
     * Actualiza una causa existente con los nuevos datos proporcionados.
     *
     * @param id identificador de la causa a actualizar.
     * @param causa objeto con los nuevos datos.
     * @return la causa actualizada.
     * @throws RuntimeException si no se encuentra la causa original.
     */
    public Causa updateCausa(Long id, Causa causa) {
        if (causaRepository.existsById(id)) {
            causa.setIdCausa(id);
            return causaRepository.save(causa);
        } else {
            throw new RuntimeException("Causa no encontrada con el id: " + id);
        }
    }
}
