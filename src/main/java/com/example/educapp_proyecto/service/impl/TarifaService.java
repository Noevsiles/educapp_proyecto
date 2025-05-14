package com.example.educapp_proyecto.service.impl;

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

    @Override
    public List<Tarifa> findAll() {
        return tarifaRepository.findAll();
    }

    @Override
    public Tarifa findById(Long id) {
        Optional<Tarifa> tarifa = tarifaRepository.findById(id);
        if (tarifa.isPresent()) {
            return tarifa.get();
        } else {
            throw new RuntimeException("Tarifa no encontrada con el id: " + id);
        }
    }

    @Override
    public Tarifa save(Tarifa tarifa) {
        return tarifaRepository.save(tarifa);
    }

    @Override
    public void deleteById(Long id) {
        if (tarifaRepository.existsById(id)) {
            tarifaRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se pudo eliminar, tarifa no encontrada con el id: " + id);
        }
    }


}
