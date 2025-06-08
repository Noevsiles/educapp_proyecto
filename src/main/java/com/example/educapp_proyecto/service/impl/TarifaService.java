package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.Educador;
import com.example.educapp_proyecto.model.Tarifa;
import com.example.educapp_proyecto.repository.TarifaRepository;
import com.example.educapp_proyecto.service.TarifaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que gestiona la lógica de negocio relacionada con las tarifas de los educadores.
 * Permite operaciones CRUD y consultas específicas por educador.
 *
 * @author Noelia Vázquez Siles
 */
@Service
public class TarifaService implements TarifaServiceInterface {

    @Autowired
    private TarifaRepository tarifaRepository;

    // Encontrar todas las tarifas
    /**
     * Recupera todas las tarifas existentes en el sistema.
     *
     * @return lista de tarifas
     */
    @Override
    public List<Tarifa> findAll() {
        return tarifaRepository.findAll();
    }

    // Encontrar una tarifa por su id
    /**
     * Busca una tarifa específica por su identificador.
     *
     * @param id identificador de la tarifa
     * @return tarifa encontrada
     * @throws RuntimeException si no se encuentra ninguna tarifa con ese ID
     */
    @Override
    public Tarifa findById(Long id) {
        Optional<Tarifa> tarifa = tarifaRepository.findById(id);
        if (tarifa.isPresent()) {
            return tarifa.get();
        } else {
            throw new RuntimeException("Tarifa no encontrada con el id: " + id);
        }
    }

    // Guardar una tarifa
    /**
     * Guarda o actualiza una tarifa en la base de datos.
     *
     * @param tarifa objeto tarifa a guardar
     * @return tarifa guardada
     */
    @Override
    public Tarifa save(Tarifa tarifa) {
        return tarifaRepository.save(tarifa);
    }

    // Eliminar tarifa por el id
    /**
     * Elimina una tarifa por su identificador.
     *
     * @param id identificador de la tarifa a eliminar
     * @throws RuntimeException si no se encuentra la tarifa
     */
    @Override
    public void deleteById(Long id) {
        if (tarifaRepository.existsById(id)) {
            tarifaRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, tarifa no encontrada con el id: " + id);
        }
    }

    // Encontrar tarifa por educador
    /**
     * Busca todas las tarifas asociadas a un educador específico.
     *
     * @param educador objeto educador
     * @return lista de tarifas asociadas
     */
    public List<Tarifa> findByEducador(Educador educador) {
        return tarifaRepository.findByEducador(educador);
    }

    // Encontrar tarifa por el email del educador
    /**
     * Busca todas las tarifas por el ID del educador.
     *
     * @param idEducador identificador del educador
     * @return lista de tarifas del educador
     */
    public List<Tarifa> findByEducadorId(Long idEducador) {
        return tarifaRepository.findByEducador_IdEducador(idEducador);
    }
}
