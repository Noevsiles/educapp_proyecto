package com.example.educapp_proyecto.service.impl;

import com.example.educapp_proyecto.model.Educador;
import com.example.educapp_proyecto.model.Tarifa;
import com.example.educapp_proyecto.repository.TarifaRepository;
import com.example.educapp_proyecto.service.TarifaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarifaService implements TarifaServiceInterface {

    @Autowired
    private TarifaRepository tarifaRepository;

    // Encontrar todas las tarifas
    @Override
    public List<Tarifa> findAll() {
        return tarifaRepository.findAll();
    }

    // Encontrar una tarifa por su id
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
    @Override
    public Tarifa save(Tarifa tarifa) {
        return tarifaRepository.save(tarifa);
    }

    // Eliminar tarifa por el id
    @Override
    public void deleteById(Long id) {
        if (tarifaRepository.existsById(id)) {
            tarifaRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, tarifa no encontrada con el id: " + id);
        }
    }

    // Encontrar tarifa por educador
    public List<Tarifa> findByEducador(Educador educador) {
        return tarifaRepository.findByEducador(educador);
    }

    // Encontrar tarifa por el email del educador
    public List<Tarifa> findByEducadorId(Long idEducador) {
        return tarifaRepository.findByEducador_IdEducador(idEducador);
    }



}
