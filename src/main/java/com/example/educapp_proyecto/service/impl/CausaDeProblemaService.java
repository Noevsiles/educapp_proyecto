package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.CausaDeProblema;
import com.example.educapp_proyecto.repository.CausaDeProblemaRepository;
import com.example.educapp_proyecto.service.CausaDeProblemaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que gestiona las operaciones CRUD sobre las causas de problemas de conducta.
 * Permite consultar, guardar, actualizar y eliminar causas de problema.
 *
 * @author Noelia Vázquez Siles
 */
@Service
public class CausaDeProblemaService implements CausaDeProblemaServiceInterface {
    @Autowired
    private CausaDeProblemaRepository causaDeProblemaRepository;

    /**
     * Obtiene todas las causas de problemas registradas en la base de datos.
     *
     * @return lista de {CausaDeProblema}.
     */
    @Override
    public List<CausaDeProblema> findAll() {
        return causaDeProblemaRepository.findAll();
    }

    // Encontrar una causa de problema por su id
    /**
     * Busca una causa de problema por su identificador.
     *
     * @param id identificador de la causa de problema.
     * @return la causa encontrada.
     * @throws RuntimeException si no se encuentra la causa con el ID dado.
     */
    @Override
    public CausaDeProblema findById(Long id) {
        Optional<CausaDeProblema> causaDeProblema = causaDeProblemaRepository.findById(id);
        if (causaDeProblema.isPresent()) {
            return causaDeProblema.get();
        } else {
            throw new RuntimeException("Causa de problema no encontrada con el id: " + id);
        }
    }

    // Guardar una causa de problema
    /**
     * Guarda una nueva causa de problema en la base de datos.
     *
     * @param causaDeProblema objeto a guardar.
     * @return la causa guardada.
     */
    @Override
    public CausaDeProblema save(CausaDeProblema causaDeProblema) {
        return causaDeProblemaRepository.save(causaDeProblema);
    }

    // Eliminar una causa de problema por su id
    /**
     * Elimina una causa de problema por su ID si existe.
     *
     * @param id identificador de la causa a eliminar.
     * @throws RuntimeException si la causa no existe.
     */
    @Override
    public void deleteById(Long id) {
        if (causaDeProblemaRepository.existsById(id)) {
            causaDeProblemaRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, causa de problema no encontrada con el id: " + id);
        }
    }

    // Actualizar una causa de problema
    /**
     * Actualiza una causa de problema existente con los nuevos datos proporcionados.
     *
     * @param id identificador de la causa a actualizar.
     * @param causaDeProblema objeto con los nuevos datos.
     * @return la causa actualizada.
     * @throws RuntimeException si no se encuentra la causa original.
     */
    public CausaDeProblema updateCausaDeProblema(Long id, CausaDeProblema causaDeProblema) {
        if (causaDeProblemaRepository.existsById(id)) {
            causaDeProblema.setIdCausaDeProblema(id);  // Aseguramos que el ID es el mismo
            return causaDeProblemaRepository.save(causaDeProblema);
        } else {
            throw new RuntimeException("Causa de problema no encontrada con el id: " + id);
        }
    }
}
